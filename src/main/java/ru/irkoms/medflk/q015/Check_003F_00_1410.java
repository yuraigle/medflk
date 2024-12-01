package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1410 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverOnkUsl(zlList, persList, check1());
    }

    private IFunctionOverOnkUsl check1() {
        return (zlList, zap, sl, onkSl, onkUsl) -> {
            Integer hirTip = onkUsl.getHirTip();
            Integer uslTip = onkUsl.getUslTip();

            if (Objects.equals(1, uslTip) && hirTip == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        };
    }
}
