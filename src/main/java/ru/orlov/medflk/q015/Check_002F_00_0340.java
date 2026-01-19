package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Ds2N;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0340 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак диспансерного наблюдения PR_DS2_N должен быть (1,2,3)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (sl.getDs2NList() != null) {
                for (Ds2N ds2N : sl.getDs2NList()) {
                    Integer pr = ds2N.getPrDs2N();
                    if (pr != null && !List.of(1, 2, 3).contains(pr)) {
                        errors.add(new FlkErr(zap, sl, null, pr));
                    }
                }
            }

            return errors;
        });
    }
}
