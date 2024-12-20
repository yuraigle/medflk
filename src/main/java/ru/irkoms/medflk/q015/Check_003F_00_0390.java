package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0390 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Вес ребёнка при рождении VNOV_D должен отсутствовать при указании VNOV_M";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vnovD = zap.getPacient().getVnovD();
            List<Integer> vnovM = zap.getZSl().getVnovMList();

            if (vnovM != null && !vnovM.isEmpty() && vnovD != null) {
                return List.of(new FlkP.Pr(zap, null, vnovD));
            }

            return List.of();
        });
    }
}
