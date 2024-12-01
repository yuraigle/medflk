package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1730 {

    public List<FlkP.Pr> check(AZap zap, APers pers) {
        if (pers == null || zap == null) return List.of();

        Integer vPolis = zap.getPacient().getVpolis();
        if (!Objects.equals(3, vPolis) && pers.getDocSer() == null) {
            return List.of(new FlkP.Pr(zap, null, null));
        }

        return List.of();
    }

}
