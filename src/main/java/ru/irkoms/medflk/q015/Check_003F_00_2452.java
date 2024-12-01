package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_2452 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            String ds1 = sl.getDs1();
            Integer pOtk = zap.getZSl().getPOtk();

            if (Objects.equals(0, pOtk) && isBlank(ds1)) {
                return List.of(new FlkP.Pr(zap, sl, ds1));
            }

            return List.of();
        });
    }
}
