package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0710 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, check1());
    }

    private IFunctionOverSl check1() {
        return (zlList, zap, sl) -> {
            Integer uslOk = zap.getZSl().getUslOk();
            Integer pPer = sl.getPPer();

            if (uslOk != null && !List.of(1, 2).contains(uslOk) && pPer != null) {
                return List.of(new FlkP.Pr(zap, sl, pPer));
            }

            return List.of();
        };
    }
}
