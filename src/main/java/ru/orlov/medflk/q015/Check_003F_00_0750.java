package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0750 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Количество койко-дней должно отсутствовать при помощи не в стационаре";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            Integer kd = sl.getKd();

            if (!List.of(1, 2).contains(uslOk) && kd != null) {
                return List.of(new FlkErr(zap, sl, null, kd));
            }

            return List.of();
        });
    }
}
