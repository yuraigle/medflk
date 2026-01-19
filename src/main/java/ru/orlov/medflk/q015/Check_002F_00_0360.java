package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0360 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак метастазов MTSTZ должен быть пустым или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            Integer mtstz = onkSl.getMtstz();

            if (mtstz != null && mtstz != 1) {
                return List.of(new FlkErr(zap, sl, null, mtstz));
            }

            return List.of();
        });
    }
}
