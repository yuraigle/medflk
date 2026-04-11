package ru.orlov.medflk.calc.hospital;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.ZSl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
public class InterruptedReasonsService {

    // 4.1 Список причин, по которым случай считается прерванным
    public Set<String> findInterruptedReasons(
            ZSl zSl, String slId, Integer kd, String nKsg, Set<String> nKsgPossible, Set<String> critList
    ) {
        Set<String> reasons = new HashSet<>();

        Sl sl0 = zSl.getSlList().stream()
                .filter(sl1 -> sl1.getSlId().equals(slId))
                .findFirst().orElseThrow();

        boolean hasLaterSl = zSl.getSlList().stream()
                .filter(sl1 -> !sl1.getSlId().equals(sl0.getSlId()))
                .anyMatch(sl1 -> !sl1.getDate1().isBefore(sl0.getDate2()));

        if (hasLaterSl) {
            reasons.add("2"); // есть последующее движение внутри Z_SL
        }

        Integer rslt = zSl.getRslt();

        if (List.of(104, 204).contains(rslt)) {
            reasons.add("2"); // перевод в другое отделение
        }
        if (List.of(103, 203).contains(rslt)) {
            reasons.add("3"); // перевод КС->ДС или ДС->КС
        }
        if (List.of(102, 202).contains(rslt)) {
            reasons.add("4"); // перевод в другую МО
        }
        if (List.of(107, 110, 207, 108, 208).contains(rslt)) {
            reasons.add("5"); // отказ от лечения
        }
        if (List.of(105, 106, 205, 206).contains(rslt)) {
            reasons.add("6"); // смерть
        }

        /*
        В случае, если длительность случая составляет 3 дня и менее, при этом
        хотя бы одна из КСГ во временной таблице не входит в перечень КСГ с
        оптимальной длительностью лечения до 3 дней включительно, – выбор КСГ
        определяется с учетом доли оплаты прерванного случая
         */
        Set<String> ksgOptimalShort = Set.of("wtf"); // todo Приложение № 8 к Программе
        boolean isShort = ksgOptimalShort.containsAll(nKsgPossible);
        if (kd <= 3 && reasons.isEmpty() && !isShort) {
            reasons.add("8"); // по кол-ву дней
        }

        if (isShortRehab(nKsg, kd, critList)) {
            reasons.add("9"); // реабилитация короче нормы
        }

        return reasons;
    }

    // 9. случаи медицинской реабилитации по КСГ с длительностью лечения менее...
    private boolean isShortRehab(String nKsg, Integer kd, Set<String> critList) {

        // для случаев мед реабилитации по этим КСГ определена норма длительности
        final List<String> reabWithSpecialKdKsgList = List.of("""
                st37.002 st37.003 st37.006 st37.007 st37.024 st37.025 st37.026 st37.027
                st37.028 st37.029 st37.030 st37.031 ds37.017 ds37.018 ds37.019
                """.split("[ \n]"));

        boolean isRehab = reabWithSpecialKdKsgList.contains(nKsg);
        boolean isChronGepBC = nKsg.matches("^ds12\\.02[0-7]$");

        if (isRehab || isChronGepBC) {
            int normDays = 0;
            for (String crit : critList) {
                if (crit.matches("^.*d\\d+$")) {
                    String s = crit.replaceAll("^.*d(\\d+)$", "$1");
                    normDays = Integer.parseInt(s);
                }
            }

            return kd < normDays;
        }

        return false;
    }

}
