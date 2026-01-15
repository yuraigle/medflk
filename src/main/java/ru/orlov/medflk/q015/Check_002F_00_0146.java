package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0146 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Социальная категория SOC должна быть (000,035,065,083)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getPacient() == null) {
                return List.of();
            }

            String soc = zap.getPacient().getSoc();
            if (soc != null && !List.of("000", "035", "065", "083").contains(soc)) {
                return List.of(new FlkErr(zap, null, null, soc));
            }

            return List.of();
        });
    }
}
