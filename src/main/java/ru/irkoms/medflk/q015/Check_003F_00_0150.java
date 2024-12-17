package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APacient;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0150 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Наименование СМО должно быть указано, если не указан код СМО и ОГРН СМО";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            APacient pac = zap.getPacient();
            String smoNam = zap.getPacient().getSmoNam();
            if (isBlank(pac.getSmo()) && isBlank(pac.getSmoOgrn()) && isBlank(smoNam)) {
                return List.of(new FlkP.Pr(zap, null, null));
            }
            return List.of();
        });
    }
}
