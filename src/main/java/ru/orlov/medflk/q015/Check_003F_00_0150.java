package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.Pacient;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0150 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Наименование СМО должно быть указано, если не указан код СМО и ОГРН СМО";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Pacient pac = zap.getPacient();
            String smoNam = zap.getPacient().getSmoNam();
            if (isBlank(pac.getSmo()) && isBlank(pac.getSmoOgrn()) && isBlank(smoNam)) {
                return List.of(new FlkP.Pr(zap, null, null));
            }
            return List.of();
        });
    }
}
