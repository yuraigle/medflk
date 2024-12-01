package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.irkoms.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_1512 extends AbstractCheck {

    // (C00.0<=DS1<D10 или D45<=DS1<D48) и USL_TIP={1,3,4}
    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            String vidVme = usl.getVidVme();

            boolean hasUslTip134 = false;
            if (sl.getOnkSl() != null && sl.getOnkSl().getOnkUslList() != null) {
                hasUslTip134 = sl.getOnkSl().getOnkUslList().stream()
                        .anyMatch(u -> u.getUslTip() != null && List.of(1, 3, 4).contains(u.getUslTip()));
            }

            if (dsIsOnkoC00ToD10OrD45ToD48(sl.getDs1()) && hasUslTip134 && isBlank(vidVme)) {
                return List.of(new FlkP.Pr(zap, sl, usl, vidVme));
            }

            return List.of();
        });
    }
}
