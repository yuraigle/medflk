package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0433 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак редукции RED_INJ должен быть пустым или 0 или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (onkUsl.getLekPrList() != null) {
                for (LekPrOnk lekPr : onkUsl.getLekPrList()) {
                    if (lekPr.getInjList() != null) {
                        for (Inj inj : lekPr.getInjList()) {
                            Integer red = inj.getRedInj();
                            if (red != null && !List.of(0, 1).contains(red)) {
                                errors.add(new FlkErr(zap, sl, null, red));
                            }
                        }
                    }
                }
            }

            return errors;
        });
    }
}
