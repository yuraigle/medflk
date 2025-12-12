package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1070 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, check1());
    }

    private IFunctionOverSl check1() {
        return (zlList, zap, sl) -> {
            AOnkSl onkSl = sl.getOnkSl();
            if (onkSl != null && onkSl.getBsa() != null && onkSl.getWei() == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }
            return List.of();
        };
    }
}
