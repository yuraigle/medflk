package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0080 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак исправленной записи PR_NOV должен быть 0 или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer prNov = zap.getPrNov();
            if (prNov == null) {
                return List.of();
            }

            if (!List.of(0, 1).contains(prNov)) {
                return List.of(new FlkErr(zap, null, null, prNov));
            }

            return List.of();
        });
    }
}
