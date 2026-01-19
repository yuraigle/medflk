package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0240 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак поступления/перевода P_PER должен быть (1,2,3,4)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer pPer = sl.getPPer();

            if (pPer != null && !List.of(1, 2, 3, 4).contains(pPer)) {
                return List.of(new FlkErr(zap, sl, null, pPer));
            }

            return List.of();
        });
    }
}
