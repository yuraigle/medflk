package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0420 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак PPTR должен быть пустым или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            Integer pptr = onkUsl.getPptr();

            if (pptr != null && pptr != 1) {
                return List.of(new FlkErr(zap, sl, null, pptr));
            }

            return List.of();
        });
    }
}
