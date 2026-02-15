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
public class Check_002F_00_0576 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код результата диагностики DIAG_RSLT должен быть 4 для условий";
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

                    if (List.of("C34", "C43").contains(ds13)
                        && onkSlHasRegNum(onkSl, "002117")
                        && Objects.equals(2, bdiag.getDiagTip())
                        && Objects.equals(2, bdiag.getDiagCode())
                        && rslt != 4
                    ) {
                        errors.add(new FlkErr(zap, sl, null, rslt));
                    }
                }
            }

            return errors;
        });
    }
}
