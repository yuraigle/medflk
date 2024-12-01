package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ACons;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.ASl;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Check_001F_00_0400 {

    public List<FlkP.Pr> check(AZap zap, APers pers) {
        Assert.notNull(zap, "ZAP must not be null");
        Assert.notNull(zap.getZSl(), "ZAP/Z_SL must not be null");
        Assert.notNull(zap.getZSl().getSlList(), "ZAP/Z_SL/SL must not be null");

        List<FlkP.Pr> errors = new ArrayList<>();

        for (ASl sl : zap.getZSl().getSlList()) {
            if (sl.getConsList() == null) continue;

            for (ACons cons : sl.getConsList()) {
                Integer prCons = cons.getPrCons();
                LocalDate d1 = zap.getZSl().getDateZ1();
                if (prCons == null || d1 == null) continue; // npe-safe

                if (!List.of(0, 1, 2, 3, 4).contains(prCons)) { // TODO N019
                    errors.add(new FlkP.Pr(zap, sl, cons.getDtCons()));
                }
            }
        }

        return errors;
    }
}
