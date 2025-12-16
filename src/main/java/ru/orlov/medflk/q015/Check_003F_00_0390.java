package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0390 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Вес ребёнка при рождении VNOV_D должен отсутствовать при указании VNOV_M";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vnovD = zap.getPacient().getVnovD();
            List<Integer> vnovM = zap.getZSl().getVnovMList();

            if (vnovM != null && !vnovM.isEmpty() && vnovD != null) {
                return List.of(new FlkErr(zap, null, null, vnovD));
            }

            return List.of();
        });
    }
}
