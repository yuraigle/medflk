package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0310 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак диспансерного наблюдения PR_D_N должен быть пустым или (1,2,3)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer prDN = sl.getPrDN();

            if (prDN != null && !List.of(1, 2, 3).contains(prDN)) {
                return List.of(new FlkErr(zap, sl, null, prDN));
            }

            return List.of();
        });
    }
}
