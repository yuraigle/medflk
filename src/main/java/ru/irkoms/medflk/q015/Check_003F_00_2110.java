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
        // так гораздо быстрее на больших реестрах
        if (zlList.getZglv().getSdZ() > 10_000) {
            long cntZap = zlList.getZapList().size();
            long cntZapUnique = zlList.getZapList().stream().distinct().count();
            if (cntZap != cntZapUnique) {
                return List.of(new FlkP.Pr(null, null, null));
            }

            return List.of();
        }

        // так медленнее, но покажем конкретный N_ZAP в ошибке
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
