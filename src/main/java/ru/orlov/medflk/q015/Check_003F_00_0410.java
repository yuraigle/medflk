package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
public class Check_003F_00_0410 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Дата направления обязательна для ДС или плановой помощи в КС";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer forPom = zap.getZSl().getForPom();
            Integer uslOk = zap.getZSl().getUslOk();
            LocalDate nprDate = zap.getZSl().getNprDate();

            if (forPom == null || uslOk == null) return List.of();

            if (uslOk == 2 && nprDate == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            } else if (uslOk == 1 && forPom == 3 && nprDate == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }
}
