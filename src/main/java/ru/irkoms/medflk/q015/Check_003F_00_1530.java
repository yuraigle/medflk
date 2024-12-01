package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Component
public class Check_003F_00_1530 {

    public List<FlkP.Pr> check(AZap zap, APers pers) {
        Assert.notNull(zap, "ZAP must not be null");
        Assert.notNull(pers, "PERS must not be null");

        List<FlkP.Pr> errors = new ArrayList<>();

        String novor = zap.getPacient().getNovor();
        List<Integer> dost = pers.getDostList() == null ? List.of() : pers.getDostList();

        if ("0".equals(novor) && !dost.contains(2) && isEmpty(pers.getFam())) {
            errors.add(new FlkP.Pr(zap, null, null));
        }

        return errors;
    }

}
