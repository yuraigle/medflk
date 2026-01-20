package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Bdiag;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0410 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак получения результата REC_RSLT должен быть пустым или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (onkSl.getBdiagList() != null) {
                for (Bdiag bdiag : onkSl.getBdiagList()) {
                    Integer rec = bdiag.getRecRslt();
                    if (rec != null && rec != 1) {
                        errors.add(new FlkErr(zap, sl, null, rec));
                    }
                }
            }

            return errors;
        });
    }
}
