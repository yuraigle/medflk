package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0520 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Причина неполной оплаты NPL должна быть пустой или (1,2,3,4)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            Integer npl = usl.getNpl();
            if (npl != null && !List.of(1, 2, 3, 4).contains(npl)) {
                return List.of(new FlkErr(zap, sl, usl, npl));
            }

            return List.of();
        });
    }
}
