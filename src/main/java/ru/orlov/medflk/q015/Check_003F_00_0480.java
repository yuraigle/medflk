package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_0480 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак особого случая должен отсутствовать";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {

        return iterateOverZap(zlList, persList, (a, zap) -> {
            PersList.Pers pers = getPersById(zap.getPacient().getIdPac());

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
