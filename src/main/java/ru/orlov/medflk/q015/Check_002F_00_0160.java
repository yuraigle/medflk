package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0160 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак \"Особый случай\" должен быть (1,2)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getZSl().getOsSluchList() == null) {
                return List.of();
            }

            List<FlkErr> errors = new ArrayList<>();

            for (Integer s : zap.getZSl().getOsSluchList()) {
                if (s != null && !List.of(1, 2).contains(s)) {
                    errors.add(new FlkErr(zap, null, null, s));
                }
            }

            return errors;
        });
    }
}
