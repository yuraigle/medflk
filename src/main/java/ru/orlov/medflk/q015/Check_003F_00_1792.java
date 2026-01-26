package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.Sank;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_1792 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код эксперта обязателен для санкций ЭКМП";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (zap.getZSl().getSankList() != null) {
                for (Sank sank : zap.getZSl().getSankList()) {
                    Integer tip = sank.getSTip();
                    boolean isEkmp = (tip > 69 && tip <= 87) || tip == 89 || tip == 94;
                    boolean hasExp = sank.getCodeExpList() != null && !sank.getCodeExpList().isEmpty();
                    if (isEkmp && !hasExp) {
                        errors.add(new FlkErr(zap, null, null, null));
                    }
                }
            }

            return errors;
        });
    }
}
