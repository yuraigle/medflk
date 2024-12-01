package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_3070 {

    public List<FlkP.Pr> check(AZap zap, APers pers) {
        List<FlkP.Pr> errors = new ArrayList<>();

        if (zap != null && pers == null) {
            errors.add(new FlkP.Pr(zap, null, null));
        }

        return errors;
    }

}
