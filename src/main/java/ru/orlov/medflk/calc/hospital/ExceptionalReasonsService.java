package ru.orlov.medflk.calc.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.calc.hospital.app9.ExceptionalAmt;
import ru.orlov.medflk.calc.hospital.app9.ExceptionalChildBirth;
import ru.orlov.medflk.calc.hospital.app9.ExceptionalIrs;
import ru.orlov.medflk.calc.hospital.app9.ExceptionalPlt;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.ZSl;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

/**
 * Приложение 9. ОСОБЕННОСТИ ФОРМИРОВАНИЯ ОТДЕЛЬНЫХ КСГ
 * Алгоритмы формирования отдельных групп, имеющих определенные особенности.
 */

@Service
@RequiredArgsConstructor
public class ExceptionalReasonsService {

    private final ExceptionalChildBirth childBirth;
    private final ExceptionalAmt amtEx;
    private final ExceptionalPlt pltEx;
    private final ExceptionalIrs irsEx;

    public String findExceptionalReason(ZSl zSl, String slId, String nKsg) {
        Sl sl = zSl.getSlList().stream()
                .filter(sl1 -> sl1.getSlId().equals(slId))
                .findFirst().orElseThrow();

        String ds1 = sl.getDs1();
        Integer uslOk = zSl.getUslOk();
        List<String> critList = requireNonNullElse(sl.getKsgKpg().getCritList(), Collections.emptyList());

        // 3. Особенности формирования КСГ акушерско-гинекологического профиля
        if (ds1.startsWith("O") && uslOk == 1 && childBirth.isExceptional(sl, nKsg)) {
            return "3";
        }

        // 13. Особенности формирования КСГ st29.007 "политравма"
        if (critList.stream().anyMatch(s -> s.startsWith("plt"))
                && pltEx.isExceptional(zSl, slId, nKsg)
        ) {
            return "13";
        }

        // 16. Особенности формирования КСГ "иммунизация против РСВИ"
        if (critList.stream().anyMatch(s -> s.startsWith("irs"))
                && irsEx.isExceptional(zSl, slId, nKsg)
        ) {
            return "16";
        }

        // 19. Особенности формирования КСГ st36.050 - st36.054 для случаев АМТ
        if (critList.stream().anyMatch(s -> s.startsWith("amt"))
                && amtEx.isExceptional(zSl, slId, nKsg)
        ) {
            return "19";
        }

        return null;
    }

}
