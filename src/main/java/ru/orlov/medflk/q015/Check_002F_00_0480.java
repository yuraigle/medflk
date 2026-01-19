package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.Sank;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0480 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Источник санкции S_IST должен быть 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (zap.getZSl().getSankList() != null) {
                for (Sank sank : zap.getZSl().getSankList()) {
                    Integer sIst = sank.getSIst();
                    if (sIst != null && sIst != 1) {
                        errors.add(new FlkErr(zap, null, null, sIst));
                    }
                }
            }

            return errors;
        });
    }
}
