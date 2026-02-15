package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Bdiag;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.orlov.medflk.OmsUtils.onkSlHasRegNum;

@Component
public class Check_002F_00_0574 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код результата диагностики DIAG_RSLT должен быть 25 для условий";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (onkSl.getBdiagList() != null) {
                for (Bdiag bdiag : onkSl.getBdiagList()) {
                    Integer rslt = bdiag.getDiagRslt();

                    String ds1 = sl.getDs1();
                    if (rslt == null || ds1 == null) continue; // npe-safe
                    String ds13 = ds1.substring(0, 3);

                    if (ds13.equals("C50")
                        && onkSlHasRegNum(onkSl, "002963")
                        && Objects.equals(2, bdiag.getDiagTip())
                        && Objects.equals(12, bdiag.getDiagCode())
                        && rslt != 25
                    ) {
                        errors.add(new FlkErr(zap, sl, null, rslt));
                    }
                }
            }

            return errors;
        });
    }
}
