package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0280 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак диспансерного наблюдения DN должен быть 1, 2, 4 или 6";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer dn = sl.getDn();
            if (dn != null && !List.of(1, 2, 4, 6).contains(dn)) {
                return List.of(new FlkErr(zap, sl, null, dn));
            }
            return List.of();
        });
    }
}
