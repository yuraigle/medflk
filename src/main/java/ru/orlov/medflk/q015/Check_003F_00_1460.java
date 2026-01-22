package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1460 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Цикл лекарственной терапии LEK_TIP_V должен отсутствовать при типе услуги не 2";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            Integer uslTip = onkUsl.getUslTip();
            Integer lekTipV = onkUsl.getLekTipV();

            if (!Objects.equals(2, uslTip) && lekTipV != null) {
                return List.of(new FlkErr(zap, sl, null, lekTipV));
            }

            return List.of();
        });
    }
}
