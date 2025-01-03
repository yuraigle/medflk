package ru.irkoms.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0640 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Профиль койки не задан для случая в КС, ДС";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            Integer profilK = sl.getProfilK();

            if (List.of(1, 2).contains(uslOk) && profilK == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
