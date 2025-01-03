package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.AOnkSl;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0980 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, check1());
    }

    private IFunctionOverSl check1() {
        return (zlList, zap, sl) -> {
            AOnkSl onkSl = sl.getOnkSl();
            if (onkSl != null) {
                Integer ds1T = onkSl.getDs1T();
                Integer stad = onkSl.getStad();
                if (ds1T != null && List.of(0, 1, 2, 3, 4).contains(ds1T) && stad == null) {
                    return List.of(new FlkP.Pr(zap, sl, null));
                }
            }

            return List.of();
        };
    }
}
