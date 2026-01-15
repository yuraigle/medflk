package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0150 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Вес недоношенного ребёнка VNOV_M должен быть от 200 до 2500 грамм";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getZSl().getVnovMList() == null) {
                return List.of();
            }

            List<FlkErr> errors = new ArrayList<>();

            for (Integer w : zap.getZSl().getVnovMList()) {
                if (w != null && (w <= 200 || w >= 2500)) {
                    errors.add(new FlkErr(zap, null, null, w));
                }
            }

            return errors;
        });
    }
}
