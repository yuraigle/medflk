package ru.irkoms.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_1480 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverOnkUsl(zlList, persList, check1());
    }

    private IFunctionOverOnkUsl check1() {
        return (zlList, zap, sl, onkSl, onkUsl) -> {
            Integer uslTip = onkUsl.getUslTip();
            Integer luchTip = onkUsl.getLuchTip();
            if (uslTip == null) return List.of();

            if (!List.of(3, 4).contains(uslTip) && luchTip != null) {
                return List.of(new FlkP.Pr(zap, sl, luchTip));
            }

            return List.of();
        };
    }
}
