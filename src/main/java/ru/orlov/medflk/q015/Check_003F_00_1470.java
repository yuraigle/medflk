package ru.orlov.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_1470 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Тип лучевой терапии LUCH_TIP обязателен при типе услуги 3 или 4";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            Integer uslTip = onkUsl.getUslTip();
            Integer luchTip = onkUsl.getLuchTip();
            if (uslTip == null) return List.of();

            if (List.of(3, 4).contains(uslTip) && luchTip == null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
