package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0250 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак подозрения на ЗНО DS_ONK должен быть 0 или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer dsOnk = sl.getDsOnk();

            if (dsOnk != null && !List.of(0, 1).contains(dsOnk)) {
                return List.of(new FlkErr(zap, sl, null, dsOnk));
            }

            return List.of();
        });
    }
}
