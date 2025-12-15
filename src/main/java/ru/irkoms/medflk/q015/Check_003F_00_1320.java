package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.Naz;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1320 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (Naz naz : sl.getNazList()) {
                Integer dsOnk = sl.getDsOnk();
                Integer nazR = naz.getNazR();
                String nazUsl = naz.getNazUsl();
                if (dsOnk == null || nazR == null) continue;

                if ((nazR != 3 || dsOnk == 0) && nazUsl != null) {
                    return List.of(new FlkP.Pr(zap, sl, nazUsl));
                }
            }

            return List.of();
        });
    }
}
