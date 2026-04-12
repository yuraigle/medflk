package ru.orlov.medflk.calc.hospital.app9;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.ZSl;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

@Service
@RequiredArgsConstructor
public class KsgExceptionalSelector {

    private final ExceptionalChildBirth childBirth;

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

        return null;
    }

}
