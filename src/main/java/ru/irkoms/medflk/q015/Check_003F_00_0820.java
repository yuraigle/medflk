package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_0820 extends AbstractCheck {

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
