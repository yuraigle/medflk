package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.OmsUtils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0962 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Услуги должны присутствовать при онко-диагнозе и типе лечения 1,3,4,6";
    }

    // (C00.0<=DS1<D10 или D45<=DS1<D48) и USL_TIP={1, 3, 4, 6}
    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getOnkSl() == null || sl.getOnkSl().getOnkUslList() == null) return List.of();

            boolean hasUsl = sl.getUslList() != null && !sl.getUslList().isEmpty();
            boolean hasUslTip1346 = sl.getOnkSl().getOnkUslList().stream()
                    .anyMatch(u -> u.getUslTip() != null && List.of(1, 3, 4, 6).contains(u.getUslTip()));
            String ds1 = sl.getDs1();

            if (dsIsOnkoC00ToD10OrD45ToD48(ds1) && hasUslTip1346 && !hasUsl) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
