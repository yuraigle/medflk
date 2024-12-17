package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZap;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

import static ru.irkoms.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_0340 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Орган выдавший документ не заполнен для полиса отличного от ПЕО";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {

        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vPolis = zap.getPacient().getVpolis();
            APers pers = getPersById(zap.getPacient().getIdPac());

            if (!Objects.equals(3, vPolis) && pers.getDocOrg() == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }

}
