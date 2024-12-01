package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1440 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverOnkUsl(zlList, persList, check1());
    }

    private IFunctionOverOnkUsl check1() {
        return (zlList, zap, sl, onkSl, onkUsl) -> {
            Integer uslTip = onkUsl.getUslTip();
            Integer lekTipL = onkUsl.getLekTipL();

            if (!Objects.equals(2, uslTip) && lekTipL != null) {
                return List.of(new FlkP.Pr(zap, sl, lekTipL));
            }

            return List.of();
        };
    }
}
