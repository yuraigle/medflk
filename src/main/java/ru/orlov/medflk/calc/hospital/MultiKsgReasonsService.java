package ru.orlov.medflk.calc.hospital;

import org.springframework.stereotype.Service;
import ru.orlov.medflk.calc.hospital.domain.CalcData;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class MultiKsgReasonsService {

    public void fillMultiKsgReasons(List<CalcData> calcData) {

        // 6. дородовая госпитализация
        List<String> newBornSlIdList = calcData.stream()
                .filter(c -> List.of("st02.003", "st02.004").contains(c.getNKsg()))
                .map(c -> c.getSl().getSlId())
                .toList(); // случаи с родоразрешением
        LocalDate dateNewBorn = calcData.stream()
                .filter(c -> newBornSlIdList.contains(c.getSl().getSlId()))
                .map(c -> c.getSl().getDate2())
                .max(Comparator.naturalOrder()).orElse(null); // дата родов

        List<String> dsPathologyShortList = List.of("O14.1", "O34.2", "O36.3", "O36.4", "O42.2");
        List<CalcData> pathologyList = calcData.stream()
                .filter(c -> c.getNKsg().equals("st02.001"))
                .filter(c -> c.getKd() >= 6 ||
                        (c.getKd() >= 2 && dsPathologyShortList.contains(c.getSl().getDs1())))
                .filter(c -> c.getSl().getDate1().isBefore(dateNewBorn))
                .toList();

        if (dateNewBorn != null && !pathologyList.isEmpty()) {
            pathologyList.forEach(c -> c.getPaymentReason().add("6")); // патология оплачивается
            calcData.stream()
                    .filter(c -> newBornSlIdList.contains(c.getSl().getSlId()))
                    .filter(c -> c.getSl().getDate2().equals(dateNewBorn))
                    .forEach(c -> c.getPaymentReason().add("6")); // и последние роды
        }

    }
}
