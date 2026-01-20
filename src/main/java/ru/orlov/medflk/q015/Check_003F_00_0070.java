package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_003F_00_0070 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Выставленная сумма должна быть больше 0";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            BigDecimal sumV = zap.getZSl().getSumv();

            if (sumV != null && sumV.compareTo(BigDecimal.ZERO) <= 0) {
                return List.of(new FlkErr(zap, null, null, sumV));
            }

            return List.of();
        });
    }
}
