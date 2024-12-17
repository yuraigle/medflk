package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0110 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Регион страхования должен быть указан для ПСО";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        if (isBlank(zlList.getSchet().getPlat())) return List.of();

        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vPolis = zap.getPacient().getVpolis();
            String stOkato = zap.getPacient().getStOkato();

            if (Objects.equals(1, vPolis) && stOkato == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }
}
