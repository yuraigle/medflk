package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_1260 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак получения диагностики REC_RSLT должен быть заполнен";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkErr> errors = new ArrayList<>();

            OnkSl onkSl = sl.getOnkSl();
            if (onkSl != null && onkSl.getBdiagList() != null) {
                for (Bdiag bDiag : onkSl.getBdiagList()) {
                    if (bDiag.getDiagRslt() != null && bDiag.getRecRslt() == null) {
                        errors.add(new FlkErr(zap, sl, null, null));
                    }
                }
            }

            return errors;
        });
    }
}
