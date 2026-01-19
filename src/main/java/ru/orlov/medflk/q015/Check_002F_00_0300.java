package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0300 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак установленного впервые диагноза DS1_PR должен быть пустым или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer ds1Pr = sl.getDs1Pr();

            if (ds1Pr != null && ds1Pr != 1) {
                return List.of(new FlkErr(zap, sl, null, ds1Pr));
            }

            return List.of();
        });
    }
}
