package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0100 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Направление на МСЭ должно отсутствовать или быть равным 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getPacient() == null) {
                return List.of();
            }

            Integer mse = zap.getPacient().getMse();
            if (mse != null && mse != 1) {
                return List.of(new FlkErr(zap, null, null, mse));
            }

            return List.of();
        });
    }
}
