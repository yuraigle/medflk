package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0220 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак детского профиля должен быть 0 или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer det = sl.getDet();

            if (det != null && !List.of(0, 1).contains(det)) {
                return List.of(new FlkErr(zap, sl, null, det));
            }

            return List.of();
        });
    }
}
