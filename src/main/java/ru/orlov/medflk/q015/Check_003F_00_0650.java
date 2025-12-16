package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0650 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Профиль койки задан для случая не стационара";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer profilK = sl.getProfilK();
            Integer uslOk = zap.getZSl().getUslOk();

            if (uslOk != null && !List.of(1, 2).contains(uslOk) && profilK != null) {
                return List.of(new FlkErr(zap, sl, null, profilK));
            }

            return List.of();
        });
    }
}
