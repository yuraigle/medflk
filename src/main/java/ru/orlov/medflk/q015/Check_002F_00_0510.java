package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0510 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак детского профиля в услуге должен быть 0 или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            Integer det = usl.getDet();
            if (det != null && !List.of(0, 1).contains(det)) {
                return List.of(new FlkErr(zap, sl, usl, det));
            }

            return List.of();
        });
    }
}
