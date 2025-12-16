package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0790 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак диспансерного наблюдения DN не заполнен при цели 1.3";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer dn = sl.getDn();
            String pCel = sl.getPCel();
            if ("1.3".equals(pCel) && dn == null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }
            return List.of();
        });
    }
}
