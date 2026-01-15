package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0140 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Вес недоношенного ребёнка VNOV_D должен быть от 200 до 2500 грамм";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getPacient() == null) {
                return List.of();
            }

            Integer w = zap.getPacient().getVnovD();
            if (w != null && (w < 200 || w > 2500)) {
                return List.of(new FlkErr(zap, null, null, w));
            }

            return List.of();
        });
    }
}
