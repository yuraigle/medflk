package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1320 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (Naz naz : sl.getNazList()) {
                Integer dsOnk = sl.getDsOnk();
                Integer nazR = naz.getNazR();
                String nazUsl = naz.getNazUsl();
                if (dsOnk == null || nazR == null) continue;

                if ((nazR != 3 || dsOnk == 0) && nazUsl != null) {
                    return List.of(new FlkErr(zap, sl, null, nazUsl));
                }
            }

            return List.of();
        });
    }
}
