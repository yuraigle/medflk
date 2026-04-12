package ru.orlov.medflk.calc.hospital;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * Приоритеты согласно приложению 8, пункт 2.5, 4й этап
 */

@Service
public class PriorityReasonsService {

    public void checkPriority(CalcData c, Set<String> possibleKsg, Integer rslt) {
        // 1) Если случай может быть отнесен к хирургической КСГ
        if (defaultString(c.getGKsg().getCodeUsl()).startsWith("A16.")) {
            c.setPriority(1);
            c.setPriorityReason("1");
            return;
        }

        // 2) Если случай может быть отнесен к профилю «Медицинская реабилитация»
        boolean isRehab = List.of("st37", "ds37").contains(c.getGKsg().getNKsg().substring(0, 4));
        if (isRehab) {
            c.setPriority(1);
            c.setPriorityReason("2");
            return;
        }

        // 3) Если случай может быть отнесен к профилю «Онкология»
        boolean isOnko = List.of("st19", "ds19").contains(c.getGKsg().getNKsg().substring(0, 4));
        if (isOnko) {
            // за исключением случаев
            boolean isExcluded = false;

            // б) которые могут быть отнесены к КСГ (...)
            List<String> excludeOnkoPrior = List.of("st08.001", "st08.002", "st08.003",
                    "ds08.001", "ds08.002", "ds08.003");
            if (excludeOnkoPrior.stream().anyMatch(possibleKsg::contains)) {
                isExcluded = true;
            }

            // г) постановки диагноза ЗНО из КСГ st27.014 (по услугам диагностики)
            if (possibleKsg.contains("st27.014")) {
                isExcluded = true;
            }

            if (!isExcluded) {
                c.setPriority(1);
                c.setPriorityReason("3");
                return;
            }
        }

        // 4) Если поле «Длительность» содержит значение «1», а случай может быть отнесен к КСГ st25.004 или ds25.001
        if (c.getGKsg().getKd() != null && c.getGKsg().getKd() == 1
                && possibleKsg.stream().anyMatch(k -> List.of("st25.004", "ds25.001").contains(k))
                && !List.of(105, 106, 205, 206).contains(rslt) // не закончившихся летальным исходом
        ) {
            c.setPriority(1);
            c.setPriorityReason("4");
        }

    }

}
