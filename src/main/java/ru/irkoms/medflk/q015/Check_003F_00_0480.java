package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0480 {

    public List<FlkP.Pr> check(AZap zap, APers pers) {
        String novor = zap.getPacient().getNovor();
        String ot = pers.getOt();
        boolean hasOsSlush = zap.getZSl().getOsSluchList() != null
                             && !zap.getZSl().getOsSluchList().isEmpty();

        if ("0".equals(novor) && !isBlank(ot) && hasOsSlush) {
            return List.of(new FlkP.Pr(zap, null, null));
        }

        return List.of();
    }
}
