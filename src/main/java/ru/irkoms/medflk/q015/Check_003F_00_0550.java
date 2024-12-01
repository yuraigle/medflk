package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0550 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            boolean hasSank = zap.getZSl().getSankList() != null
                              && !zap.getZSl().getSankList().isEmpty();

            if (!hasSank && zap.getZSl().getOplata() != null && zap.getZSl().getOplata() > 0) {
                return List.of(new FlkP.Pr(zap, null, zap.getZSl().getOplata()));
            }

            return List.of();
        });
    }

}
