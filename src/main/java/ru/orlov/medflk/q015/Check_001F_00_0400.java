package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Cons;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_001F_00_0400 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Цель проведения консилиума PR_CONS должна быть найдена в справочнике N019";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (sl.getConsList() != null) {
                for (Cons cons : sl.getConsList()) {
                    Integer prCons = cons.getPrCons();
                    if (prCons != null && !List.of(0, 1, 2, 3, 4).contains(prCons)) { // TODO N019
                        errors.add(new FlkErr(zap, sl, null, prCons));
                    }
                }
            }

            return errors;
        });
    }
}
