package ru.orlov.medflk.calc.hospital;

import org.springframework.stereotype.Service;
import ru.orlov.medflk.calc.hospital.domain.CalcData;
import ru.orlov.medflk.jaxb.Sl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MultiKsgReasonsService {

    public void fillMultiKsgReasons(List<CalcData> calcData) {
        String mainSlId = guessMainSlId(calcData);

        // 1. перевод с новой группой диагнозов
        Set<String> dsGrSet = new HashSet<>();
        Set<Integer> nWithNewDsGr = new HashSet<>();
        String lastDsGr = null;
        for (CalcData c : calcData.stream().sorted(Comparator.comparing(CalcData::getN)).toList()) {
            String dsGr = c.getSl().getDs1().substring(0, 1);
            if (!dsGrSet.contains(dsGr)) {
                dsGrSet.add(dsGr);
                nWithNewDsGr.add(c.getN());
                lastDsGr = dsGr;
            }
        }
        // оба случая лечения подлежат оплате
        if (nWithNewDsGr.size() > 1) {
            calcData.stream().filter(c -> nWithNewDsGr.contains(c.getN()))
                    .forEach(c -> c.getPaymentReason().add("1"));
        }
        // при этом случай до перевода относится к прерванным по 4.1.2
        String finalLastDsGr = lastDsGr;
        calcData.stream().filter(c -> c.getPaymentReason().contains("1"))
                .filter(c -> !c.getSl().getDs1().substring(0, 1).equals(finalLastDsGr))
                .forEach(c -> c.getInterruptReasons().add("2"));

        // 6. дородовая госпитализация >=6 дней
        List<String> newBornSlIdList = calcData.stream()
                .filter(c -> List.of("st02.003", "st02.004").contains(c.getNKsg()))
                .map(c -> c.getSl().getSlId())
                .toList(); // случаи с родоразрешением
        LocalDate dateNewBorn = calcData.stream()
                .filter(c -> newBornSlIdList.contains(c.getSl().getSlId()))
                .map(c -> c.getSl().getDate2())
                .max(Comparator.naturalOrder()).orElse(null); // дата родов

        if (dateNewBorn != null) {
            List<String> dsPathologyShortList = List.of("O14.1", "O34.2", "O36.3", "O36.4", "O42.2");
            List<CalcData> pathologyList = calcData.stream()
                    .filter(c -> c.getNKsg().equals("st02.001"))
                    .filter(c -> c.getKd() >= 6 ||
                            (c.getKd() >= 2 && dsPathologyShortList.contains(c.getSl().getDs1())))
                    .filter(c -> c.getSl().getDate1().isBefore(dateNewBorn))
                    .toList();

            if (!pathologyList.isEmpty()) {
                pathologyList.forEach(c -> c.getPaymentReason().add("6")); // патология оплачивается
                calcData.stream()
                        .filter(c -> newBornSlIdList.contains(c.getSl().getSlId()))
                        .filter(c -> c.getSl().getDate2().equals(dateNewBorn))
                        .forEach(c -> c.getPaymentReason().add("6")); // и последние роды
            }
        }

        // 9. проведение антимикробной терапии
        List<CalcData> amtList = calcData.stream()
                .filter(c -> c.getNKsg().matches("^st36\\.05[0-4]$"))
                .toList();
        if (!amtList.isEmpty()) {
            amtList.forEach(c -> c.getPaymentReason().add("9")); // оплачивается амт
            calcData.stream().filter(c -> c.getSl().getSlId().equals(mainSlId))
                    .forEach(c -> c.getPaymentReason().add("0")); // и основной случай
        }

    }

    // "Основная" КСГ, оказание медицинской помощи по которой послужило поводом для госпитализации
    private String guessMainSlId(List<CalcData> calcData) {
        return calcData.stream().map(CalcData::getSl)
                .sorted(Comparator.comparing(Sl::getDate1) // самый ранний
                        .thenComparing(Comparator.comparing(Sl::getDate2).reversed())
                        // если даты совпадают, "основной" у ЕЦП имеет длинный ID
                        .thenComparing(Comparator.comparingInt((Sl s) -> s.getSlId().length()).reversed()))
                .map(Sl::getSlId)
                .findFirst().orElse(null);
    }
}
