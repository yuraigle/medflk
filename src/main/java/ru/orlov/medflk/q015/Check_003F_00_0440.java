package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0440 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Количество койко-дней должно отсутствовать при помощи не в стационаре";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer kdZ = zap.getZSl().getKdZ();
            Integer uslOk = zap.getZSl().getUslOk();

            if (uslOk != null && !List.of(1, 2).contains(uslOk) && kdZ != null) {
                return List.of(new FlkErr(zap, null, null, kdZ));
            }

            return List.of();
        });
    }
}
