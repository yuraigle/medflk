package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static ru.irkoms.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0942 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getOnkSl() == null || sl.getOnkSl().getOnkUslList() == null) return List.of();

            boolean hasUsl = sl.getUslList() != null && !sl.getUslList().isEmpty();
            boolean hasUslTip134 = sl.getOnkSl().getOnkUslList().stream()
                    .anyMatch(u -> u.getUslTip() != null && List.of(1, 3, 4).contains(u.getUslTip()));
            String ds1 = sl.getDs1();

            if (dsIsOnkoC00ToD10OrD45ToD48(ds1) && hasUslTip134 && !hasUsl) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
