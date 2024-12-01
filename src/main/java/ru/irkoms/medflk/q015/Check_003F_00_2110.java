package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_2110 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer nZap = zap.getNZap();

            boolean isUnique = zlList.getZapList().stream()
                    .filter(z -> Objects.equals(z.getNZap(), nZap))
                    .count() == 1;

            if (!isUnique) {
                return List.of(new FlkP.Pr(zap, null, nZap));
            }

            return List.of();
        });
    }
}
