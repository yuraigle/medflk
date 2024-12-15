package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0460 extends AbstractCheckZapWithPers {

    @Override
    public List<FlkP.Pr> check(AZap zap, APers pers) {
        String novor = zap.getPacient().getNovor();
        String ot = pers.getOt();
        boolean hasOsSlush = zap.getZSl().getOsSluchList() != null
                             && !zap.getZSl().getOsSluchList().isEmpty();

        // (PERS.OT отсутствует и NOVOR=0)
        // или (PERS.OT_P отсутствует и NOVOR<>0)
        // или (NOVOR<>0 и последний символ NOVOR не равен 1)

        if ("0".equals(novor) && isBlank(ot) && !hasOsSlush) {
            return List.of(new FlkP.Pr(zap, null, null));
        } else if (!"0".equals(novor) && isBlank(pers.getOtP()) && !hasOsSlush) {
            return List.of(new FlkP.Pr(zap, null, null));
        } else if (!"0".equals(novor) && !novor.endsWith("1") && !hasOsSlush) {
            return List.of(new FlkP.Pr(zap, null, null));
        }

        return List.of();
    }
}
