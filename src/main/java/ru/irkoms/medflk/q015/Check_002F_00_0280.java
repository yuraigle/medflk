package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_002F_00_0280 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак диспансерного наблюдения DN должен быть 1, 2, 4 или 6";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer dn = sl.getDn();
            if (dn != null && !List.of(1, 2, 4, 6).contains(dn)) {
                return List.of(new FlkP.Pr(zap, sl, dn));
            }
            return List.of();
        });
    }
}
