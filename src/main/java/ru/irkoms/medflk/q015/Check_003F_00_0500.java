package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_0500 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак внутрибольничного перевода должен присутствовать при P_PER=4";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vbP = zap.getZSl().getVbP();
            Integer idsp = zap.getZSl().getIdsp();
            boolean hasPper4 = zap.getZSl().getSlList().stream()
                    .anyMatch(sl -> Objects.equals(4, sl.getPPer()));

            if (Objects.equals(33, idsp) && hasPper4 && !Objects.equals(1, vbP)) {
                return List.of(new FlkP.Pr(zap, null, vbP));
            }

            return List.of();
        });
    }
}
