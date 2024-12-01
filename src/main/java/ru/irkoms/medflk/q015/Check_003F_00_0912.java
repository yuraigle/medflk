package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static ru.irkoms.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0912 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, check1());
    }

    private IFunctionOverSl check1() {
        return (zlList, zap, sl) -> {
            String ds1 = sl.getDs1();
            Integer uslOk = zap.getZSl().getUslOk();
            int reab = sl.getReab() == null ? 0 : sl.getReab();
            if (ds1 == null || uslOk == null) return List.of();

            boolean isDsOnk = dsIsOnkoC00ToD10OrD45ToD48(ds1);
            if (sl.getOnkSl() != null && (!isDsOnk || uslOk == 4 || reab == 1)) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        };
    }
}
