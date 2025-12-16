package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_0950 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Услуги присутствуют при признаке отказа =1";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer pOtk = zap.getZSl().getPOtk();

            boolean hasUsl = sl.getUslList() != null && !sl.getUslList().isEmpty();
            if (Objects.equals(1, pOtk) && hasUsl) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
