package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.Sank;
import ru.orlov.medflk.jaxb.ZlList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_1781 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код причины отказа S_OSN обязателен при сумме санкции <> 0";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (zap.getZSl().getSankList() != null) {
                for (Sank sank : zap.getZSl().getSankList()) {
                    BigDecimal sum = sank.getSSum();
                    if (sum != null && sum.compareTo(BigDecimal.ZERO) != 0 && sank.getSOsn() == null) {
                        errors.add(new FlkErr(zap, null, null, null));
                    }
                }
            }

            return errors;
        });
    }
}
