package ru.irkoms.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_0750 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Количество койко-дней должно отсутствовать при помощи не в стационаре";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            Integer kd = sl.getKd();

            if (!List.of(1, 2).contains(uslOk) && kd != null) {
                return List.of(new FlkP.Pr(zap, sl, kd));
            }

            return List.of();
        });
    }
}
