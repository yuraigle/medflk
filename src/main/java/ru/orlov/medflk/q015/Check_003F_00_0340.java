package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_0340 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Орган выдавший документ не заполнен для полиса отличного от ПЕО";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {

        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vPolis = zap.getPacient().getVpolis();
            PersList.Pers pers = getPersById(zap.getPacient().getIdPac());

            if (!Objects.equals(3, vPolis) && pers.getDocOrg() == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }

}
