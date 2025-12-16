package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0882 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Нет сведений об онкологии для случая из файла C";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            String ds1 = sl.getDs1();
            Integer uslOk = zap.getZSl().getUslOk();
            int reab = sl.getReab() == null ? 0 : sl.getReab();
            int dsOnk = sl.getDsOnk() == null ? 0 : sl.getDsOnk();
            if (ds1 == null || uslOk == null) return List.of();

            boolean isDsOnk = dsIsOnkoC00ToD10OrD45ToD48(ds1);
            if (sl.getOnkSl() == null && isDsOnk && uslOk != 4 && reab != 1 && dsOnk == 0) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
