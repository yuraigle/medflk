package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.util.List;

@Component
public class Check_003F_00_1680 {

    public List<FlkP.Pr> check(AZap zap, APers pers) {
        Assert.notNull(zap, "ZAP must not be null");
        Assert.notNull(zap.getZSl(), "ZAP/Z_SL must not be null");
        Assert.notNull(zap.getPacient(), "ZAP/PACIENT must not be null");

        String novor = zap.getPacient().getNovor();
        Integer wP = pers.getWP();

        if ("0".equals(novor) && wP != null) {
            return List.of(new FlkP.Pr(pers, null));
        }

        return List.of();
    }
}
