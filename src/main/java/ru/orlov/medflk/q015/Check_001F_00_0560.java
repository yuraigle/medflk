package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N020Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.LekPrOnk;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0560 extends AbstractCheck {

    private final N020Service n020Service;

    @Override
    public String getErrorMessage() {
        return "Идентификатор лекарственного препарата REGNUM должен быть найден в справочнике N020";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (onkUsl.getLekPrList() != null) {
                for (LekPrOnk lekPrOnk : onkUsl.getLekPrList()) {
                    String regnum = lekPrOnk.getRegnum();
                    if (regnum != null && !n020Service.isValidRegnumOnDate(regnum, d2)) {
                        errors.add(new FlkErr(zap, sl, null, regnum));
                    }
                }
            }

            return errors;
        });
    }
}
