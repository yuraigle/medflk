package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_002F_00_0390 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Площадь тела BSA должна быть до 6.00 м2";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            BigDecimal bsa = onkSl.getBsa();

            if (bsa != null && bsa.compareTo(BigDecimal.valueOf(6.00)) >= 0) {
                return List.of(new FlkErr(zap, sl, null, bsa));
            }

            return List.of();
        });
    }
}
