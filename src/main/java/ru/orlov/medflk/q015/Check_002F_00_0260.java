package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0260 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "В файле общего типа Д1 не должен встречаться онко-диагноз";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            String ds1 = sl.getDs1();

            if (ds1 != null) {
                String dsGroup =  ds1.substring(0, 3).toUpperCase();
                if (dsGroup.compareTo("C00") >= 0 && dsGroup.compareTo("D09") <= 0) {
                    return List.of(new FlkErr(zap, sl, null, ds1));
                }
            }

            return List.of();
        });
    }
}
