package ru.orlov.medflk.calc.hospital.app9;

import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.ZSl;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExceptionalAmt {

    public boolean isExceptional(ZSl zSl, String slId, String ksg) {
        // в сочетании с основной КСГ, оказание МП по которой послужило поводом для госпитализации
        String mainSlId = guessMainSlId(zSl);

        if (slId.equals(mainSlId)) {
            return false;
        }

        Sl sl = zSl.getSlList().stream()
                .filter(sl1 -> sl1.getSlId().equals(slId)).findFirst().orElseThrow();

        List<String> critList = sl.getKsgKpg() != null && sl.getKsgKpg().getCritList() != null ?
                sl.getKsgKpg().getCritList() : List.of();

        // Отнесение к КСГ st36.050 - st36.054 осуществляется по коду DKK
        Set<Integer> amtList = critList.stream()
                .filter(s -> s.matches("^amt[0-9]+$"))
                .map(s -> Integer.parseInt(s.substring(3)))
                .collect(Collectors.toSet());

        return amtList.stream().anyMatch(i -> i >= 50 && i <= 195)
                && ksg.matches("^st36\\.05[0-4]$");
    }

    private String guessMainSlId(ZSl zSl) {
        return zSl.getSlList()
                .stream()
                .sorted(Comparator.comparing(Sl::getDate1) // самый ранний
                        .thenComparing(Comparator.comparing(Sl::getDate2).reversed())
                        // если даты совпадают, "основной" у ЕЦП имеет длинный ID
                        .thenComparing(Comparator.comparingInt((Sl s) -> s.getSlId().length()).reversed()))
                .map(Sl::getSlId)
                .findFirst().orElse(null);
    }
}
