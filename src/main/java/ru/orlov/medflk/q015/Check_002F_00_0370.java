package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_002F_00_0370 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Масса тела WEI должна быть до 500кг";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            BigDecimal wei = onkSl.getWei();

            if (wei != null && wei.compareTo(BigDecimal.valueOf(500.0)) >= 0) {
                return List.of(new FlkErr(zap, sl, null, wei));
            }

            return List.of();
        });
    }
}
