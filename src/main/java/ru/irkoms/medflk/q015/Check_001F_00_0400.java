package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.Cons;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_001F_00_0400 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Цель проведения консилиума PR_CONS не из справочника N019";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkP.Pr> errors = new ArrayList<>();
            if (sl.getConsList() != null) {
                for (Cons cons : sl.getConsList()) {
                    Integer prCons = cons.getPrCons();
                    if (prCons != null && !List.of(0, 1, 2, 3, 4).contains(prCons)) { // TODO N019
                        errors.add(new FlkP.Pr(zap, sl, prCons));
                    }
                }
            }
            return errors;
        });
    }
}
