package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_1240 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Дата проведения консилиума указана при цели не 1,2 или 3";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkP.Pr> errors = new ArrayList<>();

            if (sl.getConsList() != null) {
                for (ACons cons : sl.getConsList()) {
                    Integer pr = cons.getPrCons();
                    if (pr != null && !List.of(1, 2, 3).contains(pr) && cons.getDtCons() != null) {
                        errors.add(new FlkP.Pr(zap, sl, cons.getDtCons()));
                    }
                }
            }

            return errors;
        });
    }
}
