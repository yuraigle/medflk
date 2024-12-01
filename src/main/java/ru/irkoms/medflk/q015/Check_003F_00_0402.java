package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class Check_003F_00_0402 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            String nprMo = zap.getZSl().getNprMo();
            LocalDate nprDate = zap.getZSl().getNprDate();

            if (nprDate == null && isNotBlank(nprMo)) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }
}
