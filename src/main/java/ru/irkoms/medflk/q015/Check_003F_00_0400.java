package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0400 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код направляющей МО обязателен для ДС или плановой помощи в КС";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer forPom = zap.getZSl().getForPom();
            Integer uslOk = zap.getZSl().getUslOk();
            String nprMo = zap.getZSl().getNprMo();

            if (forPom == null || uslOk == null) return List.of();

            if (uslOk == 2 && isBlank(nprMo)) {
                return List.of(new FlkP.Pr(zap, null, nprMo));
            } else if (uslOk == 1 && forPom == 3 && isBlank(nprMo)) {
                return List.of(new FlkP.Pr(zap, null, nprMo));
            }

            return List.of();
        });
    }
}
