package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static ru.irkoms.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_3070 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Персона не найдена в L-файле";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            APers pers = getPersById(zap.getPacient().getIdPac());

            if (pers == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }

}
