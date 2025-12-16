package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_0510 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак внутрибольничного перевода должен отстутсвовать при P_PER<>4";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vbP = zap.getZSl().getVbP();
            Integer idsp = zap.getZSl().getIdsp();
            boolean hasPper4 = zap.getZSl().getSlList().stream()
                    .anyMatch(sl -> Objects.equals(4, sl.getPPer()));

            if ((!Objects.equals(33, idsp) || !hasPper4) && vbP != null) {
                return List.of(new FlkP.Pr(zap, null, vbP));
            }

            return List.of();
        });
    }
}
