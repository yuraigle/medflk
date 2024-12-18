package ru.irkoms.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static ru.irkoms.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0772 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Характер заболевания не указан при онко-диагнозе";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull String ds1 = sl.getDs1();
            Integer cZab = sl.getCZab();

            if (dsIsOnkoC00ToD10OrD45ToD48(ds1) && cZab == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
