package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_1090 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverOnkSl(zlList, persList, check1());
    }

    private IFunctionOverOnkSl check1() {
        return (zlList, zap, sl, onkSl) -> {
            Integer uslOk = zap.getZSl().getUslOk();
            Integer ds1T = onkSl.getDs1T();
            if (uslOk == null || ds1T == null) return List.of();

            boolean hasOnkUsl = onkSl.getOnkUslList() != null && !onkSl.getOnkUslList().isEmpty();
            if (List.of(1, 2).contains(uslOk) && List.of(0, 1, 2).contains(ds1T) && !hasOnkUsl) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        };
    }
}
