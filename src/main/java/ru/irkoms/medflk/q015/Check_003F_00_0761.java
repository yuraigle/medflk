package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0761 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer uslOk = zap.getZSl().getUslOk();
            Integer cZab = sl.getCZab();
            String ds1 = sl.getDs1();

            if (uslOk == null || ds1 == null) return List.of();

            ds1 = ds1.toUpperCase();

            // USL_OK=3 и DS1<Z00.0 и DS1<>U11 и DS1<>U11.9
            if (cZab == null && uslOk == 3
                    && !ds1.equals("U11") && !ds1.equals("U11.9")
                    && ds1.compareTo("Z00.0") < 0
            ) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
