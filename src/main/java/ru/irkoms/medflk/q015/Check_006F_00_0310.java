package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.List;

@Component
public class Check_006F_00_0310 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            LocalDate nprDate = zap.getZSl().getNprDate();
            LocalDate d1 = zap.getZSl().getDateZ1();

            if (d1 != null && nprDate != null && nprDate.isAfter(d1)) {
                return List.of(new FlkP.Pr(zap, null, nprDate));
            }

            return null;
        });
    }
}
