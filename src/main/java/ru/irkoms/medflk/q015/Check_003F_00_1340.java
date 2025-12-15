package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.Naz;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
public class Check_003F_00_1340 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (Naz naz : sl.getNazList()) {
                LocalDate naprDate = naz.getNaprDate();
                Integer nazR = naz.getNazR();
                Integer dsOnk = sl.getDsOnk();

                if (dsOnk == null || nazR == null) continue;

                if ((!List.of(2, 3).contains(nazR) || dsOnk == 0) && naprDate != null) {
                    return List.of(new FlkP.Pr(zap, sl, naprDate));
                }
            }

            return List.of();
        });
    }
}
