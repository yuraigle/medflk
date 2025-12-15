package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_1260 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkP.Pr> errors = new ArrayList<>();

            OnkSl onkSl = sl.getOnkSl();
            if (onkSl != null && onkSl.getBdiagList() != null) {
                for (Bdiag bDiag : onkSl.getBdiagList()) {
                    if (bDiag.getDiagRslt() != null && bDiag.getRecRslt() == null) {
                        errors.add(new FlkP.Pr(zap, sl, null));
                    }
                }
            }

            return errors;
        });
    }
}
