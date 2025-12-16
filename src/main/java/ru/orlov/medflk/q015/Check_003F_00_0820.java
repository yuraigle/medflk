package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_0820 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак реабилитации REAB заполнен при неплановой форме помощи FOR_POM<>3";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer reab = sl.getReab();
            Integer forPom = zap.getZSl().getForPom();

            if (!Objects.equals(3, forPom) && reab != null) {
                return List.of(new FlkP.Pr(zap, sl, reab));
            }

            return List.of();
        });
    }
}
