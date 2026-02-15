package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.OmsUtils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0912 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Указаны сведения онкологии при не онко диагнозе или реабилитации или СМП";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            String ds1 = sl.getDs1();
            Integer uslOk = zap.getZSl().getUslOk();
            int reab = sl.getReab() == null ? 0 : sl.getReab();
            if (ds1 == null || uslOk == null) return List.of();

            boolean isDsOnk = dsIsOnkoC00ToD10OrD45ToD48(ds1);
            if (sl.getOnkSl() != null && (!isDsOnk || uslOk == 4 || reab == 1)) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
