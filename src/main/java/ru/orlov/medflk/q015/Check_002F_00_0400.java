package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Bdiag;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0400 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Тип диагностического показателя DIAG_TIP должен быть (1,2)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (onkSl.getBdiagList() != null) {
                for (Bdiag bdiag : onkSl.getBdiagList()) {
                    Integer tip = bdiag.getDiagTip();
                    if (tip != null && !List.of(1, 2).contains(tip)) {
                        errors.add(new FlkErr(zap, sl, null, tip));
                    }
                }
            }

            return errors;
        });
    }
}
