package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0440 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, check1());
    }

    private IFunctionOverZap check1() {
        return (zlList, zap) -> {
            Integer kdZ = zap.getZSl().getKdZ();
            Integer uslOk = zap.getZSl().getUslOk();

            if (uslOk != null && !List.of(1, 2).contains(uslOk) && kdZ != null) {
                return List.of(new FlkP.Pr(zap, null, kdZ));
            }

            return List.of();
        };
    }
}
