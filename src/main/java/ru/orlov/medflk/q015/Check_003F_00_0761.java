package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0761 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Характер заболевания не указан для случая АПУ";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            @NonNull String ds1 = sl.getDs1();
            Integer cZab = sl.getCZab();

            // USL_OK=3 и DS1<Z00.0 и DS1<>U11 и DS1<>U11.9
            if (uslOk == 3 && ds1.compareTo("Z00.0") < 0
                    && !ds1.equals("U11") && !ds1.equals("U11.9")
                    && cZab == null
            ) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
