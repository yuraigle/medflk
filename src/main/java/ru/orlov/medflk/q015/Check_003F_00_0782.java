package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0782 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Характер заболевания не указан при онко-диагнозе";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            @NonNull String ds1 = sl.getDs1();
            Integer cZab = sl.getCZab();

            if (dsIsOnkoC00ToD10OrD45ToD48(ds1) && uslOk != 4 && cZab == null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
