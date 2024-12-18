package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0650 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Профиль койки задан для случая не стационара";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer profilK = sl.getProfilK();
            Integer uslOk = zap.getZSl().getUslOk();

            if (uslOk != null && !List.of(1, 2).contains(uslOk) && profilK != null) {
                return List.of(new FlkP.Pr(zap, sl, profilK));
            }

            return List.of();
        });
    }
}
