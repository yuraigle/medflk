package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ABdiag;
import ru.irkoms.medflk.jaxb.meta.AOnkSl;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_1260 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, check1());
    }

    private IFunctionOverSl check1() {
        return (zlList, zap, sl) -> {
            List<FlkP.Pr> errors = new ArrayList<>();

            AOnkSl onkSl = sl.getOnkSl();
            if (onkSl != null && onkSl.getBdiagList() != null) {
                for (ABdiag bDiag : onkSl.getBdiagList()) {
                    if (bDiag.getDiagRslt() != null && bDiag.getRecRslt() == null) {
                        errors.add(new FlkP.Pr(zap, sl, null));
                    }
                }
            }

            return errors;
        };
    }
}
