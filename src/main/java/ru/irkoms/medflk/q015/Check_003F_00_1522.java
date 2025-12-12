package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.irkoms.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_1522 extends AbstractCheck {

    // (C00.0<=DS1<D10 или D45<=DS1<D48) и USL_TIP={1,3,4,6}
    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            String vidVme = usl.getVidVme();

            boolean hasUslTip1346 = false;
            if (sl.getOnkSl() != null && sl.getOnkSl().getOnkUslList() != null) {
                hasUslTip1346 = sl.getOnkSl().getOnkUslList().stream()
                        .anyMatch(u -> u.getUslTip() != null && List.of(1, 3, 4, 6).contains(u.getUslTip()));
            }

            if (dsIsOnkoC00ToD10OrD45ToD48(sl.getDs1()) && hasUslTip1346 && isBlank(vidVme)) {
                return List.of(new FlkP.Pr(zap, sl, usl, vidVme));
            }

            return List.of();
        });
    }
}
