package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APacient;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_2170 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            APacient pac = zap.getPacient();

            if (Objects.equals(3, pac.getVpolis()) && !isBlank(pac.getEnp())) {
                return List.of(); // OK
            } else if (isBlank(pac.getNpolis())) {
                return List.of(new FlkP.Pr(zap, null, null)); // ошибка: надо NPOLIS
            }

            return List.of();
        });
    }
}
