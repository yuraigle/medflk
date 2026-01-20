package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_0450 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Вес при рождении VNOV_M должен отсутствовать при заполненном VNOV_D";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            List<FlkErr> errors = new ArrayList<>();

            Integer vnovD = zap.getPacient().getVnovD();
            if (zap.getZSl().getVnovMList() != null) {
                for (Integer w : zap.getZSl().getVnovMList()) {
                    if (vnovD != null && w != null) {
                        errors.add(new FlkErr(zap, null, null, w));
                    }
                }
            }

            return errors;
        });
    }
}
