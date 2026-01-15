package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0090 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Группа инвалидности INV должна быть 0-4";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getPacient() == null) {
                return List.of();
            }

            Integer inv = zap.getPacient().getInv();
            if (inv != null && !List.of(0, 1, 2, 3, 4).contains(inv)) {
                return List.of(new FlkErr(zap, null, null, inv));
            }

            return List.of();
        });
    }
}
