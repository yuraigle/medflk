package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0380 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Рост HEI должен быть до 260см";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            Integer hei = onkSl.getHei();

            if (hei != null && hei >= 260) {
                return List.of(new FlkErr(zap, sl, null, hei));
            }

            return List.of();
        });
    }
}
