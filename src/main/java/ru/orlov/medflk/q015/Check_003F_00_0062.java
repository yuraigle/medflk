package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

import static ru.orlov.medflk.OmsUtils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0062 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Законченный случай должен содержать онко-диагноз или подозрение на ЗНО";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            boolean hasOnk = false;

            for (Sl sl : zap.getZSl().getSlList()) {
                if (Objects.equals(1, sl.getDsOnk()) || dsIsOnkoC00ToD10OrD45ToD48(sl.getDs1())) {
                    hasOnk = true;
                    break;
                }
            }

            if (!hasOnk) {
                return List.of(new FlkErr(zap, null, null, null));
            }

            return List.of();
        });
    }
}
