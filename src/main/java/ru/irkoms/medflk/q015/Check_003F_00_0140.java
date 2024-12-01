package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APacient;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.right;

@Component
public class Check_003F_00_0140 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        String typeMd = right(zlList.getClass().getSimpleName(), 1);
        if ("H".equals(typeMd)) return List.of(); // в версии 4.0 поля нет по Q018

        return iterateOverZap(zlList, persList, (a, zap) -> {
            APacient pac = zap.getPacient();
            // если SMO отсутствует, SMO_OK должен быть задан
            if (isBlank(pac.getSmo()) && isBlank(pac.getSmoOk())) {
                return List.of(new FlkP.Pr(zap, null, null));
            }
            return List.of();
        });
    }
}
