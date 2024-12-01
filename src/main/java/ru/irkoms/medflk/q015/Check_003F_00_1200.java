package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ANapr;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1200 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNaprList() == null) return List.of();

            for (ANapr napr : sl.getNaprList()) {
                Integer naprV = napr.getNaprV();
                if (!Objects.equals(3, naprV) && napr.getMetIssl() != null) {
                    return List.of(new FlkP.Pr(zap, sl, napr.getMetIssl()));
                }
            }

            return List.of();
        });
    }
}
