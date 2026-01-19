package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_002F_00_0350 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Вид направления/назначения NAZ_R должен быть 1-6";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (sl.getNazList() != null) {
                for (Naz naz : sl.getNazList()) {
                    Integer nazR = naz.getNazR();
                    if (nazR != null && !List.of(1, 2, 3, 4, 5, 6).contains(nazR)) {
                        errors.add(new FlkErr(zap, sl, null, nazR));
                    }
                }
            }

            return errors;
        });
    }
}
