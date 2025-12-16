package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_0460 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак особого случая не заполнен";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {

        return iterateOverZap(zlList, persList, (a, zap) -> {
            PersList.Pers pers = getPersById(zap.getPacient().getIdPac());

            String novor = zap.getPacient().getNovor();
            String ot = pers.getOt();
            boolean hasOsSlush = zap.getZSl().getOsSluchList() != null
                    && !zap.getZSl().getOsSluchList().isEmpty();

            // (PERS.OT отсутствует и NOVOR=0)
            // или (PERS.OT_P отсутствует и NOVOR<>0)
            // или (NOVOR<>0 и последний символ NOVOR не равен 1)

            if ("0".equals(novor) && isBlank(ot) && !hasOsSlush) {
                return List.of(new FlkErr(zap, null, null, null));
            } else if (!"0".equals(novor) && isBlank(pers.getOtP()) && !hasOsSlush) {
                return List.of(new FlkErr(zap, null, null, null));
            } else if (!"0".equals(novor) && !novor.endsWith("1") && !hasOsSlush) {
                return List.of(new FlkErr(zap, null, null, null));
            }

            return List.of();
        });
    }
}
