package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Ds2N;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0330 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак установленного впервые диагноза DS2_PR должен быть пустым или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (sl.getDs2NList() != null) {
                for (Ds2N ds2N : sl.getDs2NList()) {
                    Integer pr = ds2N.getDs2Pr();
                    if (pr != null && pr != 1) {
                        errors.add(new FlkErr(zap, sl, null, pr));
                    }
                }
            }

            return errors;
        });
    }
}
