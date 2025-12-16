package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.OnkSl;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0980 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Стадия заболевания не заполнена";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            OnkSl onkSl = sl.getOnkSl();
            if (onkSl != null) {
                Integer ds1T = onkSl.getDs1T();
                Integer stad = onkSl.getStad();
                if (ds1T != null && List.of(0, 1, 2, 3, 4).contains(ds1T) && stad == null) {
                    return List.of(new FlkErr(zap, sl, null, null));
                }
            }

            return List.of();
        });
    }

}
