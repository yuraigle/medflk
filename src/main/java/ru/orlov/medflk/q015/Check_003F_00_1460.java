package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1460 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            Integer uslTip = onkUsl.getUslTip();
            Integer lekTipV = onkUsl.getLekTipV();

            if (!Objects.equals(2, uslTip) && lekTipV != null) {
                return List.of(new FlkP.Pr(zap, sl, lekTipV));
            }

            return List.of();
        });
    }
}
