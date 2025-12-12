package ru.irkoms.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0710 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак поступления/перевода заполнен для случая не стационара";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            Integer pPer = sl.getPPer();

            if (!List.of(1, 2).contains(uslOk) && pPer != null) {
                return List.of(new FlkP.Pr(zap, sl, pPer));
            }

            return List.of();
        });
    }
}
