package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.irkoms.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_0480 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак особого случая должен отсутствовать";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {

        return iterateOverZap(zlList, persList, (a, zap) -> {
            APers pers = getPersById(zap.getPacient().getIdPac());

            String novor = zap.getPacient().getNovor();
            String ot = pers.getOt();
            boolean hasOsSlush = zap.getZSl().getOsSluchList() != null
                    && !zap.getZSl().getOsSluchList().isEmpty();

            if ("0".equals(novor) && !isBlank(ot) && hasOsSlush) {
                Integer osSluch = zap.getZSl().getOsSluchList().get(0);
                return List.of(new FlkP.Pr(zap, null, osSluch));
            }

            return List.of();
        });
    }
}
