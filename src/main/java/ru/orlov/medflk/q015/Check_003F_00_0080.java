package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0080 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Серия полиса должна быть заполнена только для ПСО";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vPolis = zap.getPacient().getVpolis();
            String sPolis = zap.getPacient().getSpolis();
            if (!Objects.equals(1, vPolis) && !isBlank(sPolis)) {
                return List.of(new FlkP.Pr(zap, null, sPolis));
            }
            return List.of();
        });
    }
}
