package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0290 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак реабилитации REAB должен быть пустым или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer reab = sl.getReab();
            if (reab != null && reab != 1) {
                return List.of(new FlkErr(zap, sl, null, reab));
            }
            return List.of();
        });
    }
}
