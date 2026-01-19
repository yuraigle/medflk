package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0320 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код классификатора VERS_SPEC должен быть V021";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            String versSpec = sl.getVersSpec();
            if (versSpec != null && !versSpec.equals("V021")) {
                return List.of(new FlkErr(zap, sl, null, versSpec));
            }
            return List.of();
        });
    }
}
