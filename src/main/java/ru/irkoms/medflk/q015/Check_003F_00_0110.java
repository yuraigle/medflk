package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class Check_003F_00_0110 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, check1());
    }

    IFunctionOverZap check1() {
        return (zlList, zap) -> {
            String plat = zlList.getSchet().getPlat();
            Integer vPolis = zap.getPacient().getVpolis();
            String stOkato = zap.getPacient().getStOkato();

            if (isNotBlank(plat) && Objects.equals(1, vPolis) && stOkato == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        };
    }

}
