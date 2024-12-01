package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ABdiag;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1250 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            if (onkSl.getBdiagList() == null) return List.of();

            for (ABdiag bDiag : onkSl.getBdiagList()) {
                Integer diagRslt = bDiag.getDiagRslt();
                Integer recRslt = bDiag.getRecRslt();

                if (Objects.equals(1, recRslt) && diagRslt == null) {
                    return List.of(new FlkP.Pr(zap, sl, null));
                }
            }

            return List.of();
        });
    }
}
