package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_1350 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (Naz naz : sl.getNazList()) {
                String naprMo = naz.getNaprMo();
                Integer nazR = naz.getNazR();
                Integer dsOnk = sl.getDsOnk();

                if (dsOnk == null || nazR == null) continue;

                if (List.of(2, 3).contains(nazR) && dsOnk == 1 && isBlank(naprMo)) {
                    return List.of(new FlkP.Pr(zap, sl, naprMo));
                }
            }

            return List.of();
        });
    }
}
