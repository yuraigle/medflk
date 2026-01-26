package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1090 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Онко-услуги должны присутствовать при лечении в стационаре";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            Integer uslOk = zap.getZSl().getUslOk();
            Integer ds1T = onkSl.getDs1T();
            if (uslOk == null || ds1T == null) return List.of();

            boolean hasOnkUsl = onkSl.getOnkUslList() != null && !onkSl.getOnkUslList().isEmpty();
            if (List.of(1, 2).contains(uslOk) && List.of(0, 1, 2).contains(ds1T) && !hasOnkUsl) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
