package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1020 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак метастазов должен отсутствовать при DS1_T<>(1,2)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            Integer ds1T = onkSl.getDs1T();
            Integer mtstz = onkSl.getMtstz();
            if (ds1T != null && !List.of(1, 2).contains(ds1T) && mtstz != null) {
                return List.of(new FlkErr(zap, sl, null, mtstz));
            }

            return List.of();
        });
    }
}
