package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Cons;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_1240 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Дата проведения консилиума должна отсутствовать при цели не 1,2,3";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            List<FlkErr> errors = new ArrayList<>();

            if (sl.getConsList() != null) {
                for (Cons cons : sl.getConsList()) {
                    Integer pr = cons.getPrCons();
                    if (pr != null && !List.of(1, 2, 3).contains(pr) && cons.getDtCons() != null) {
                        errors.add(new FlkErr(zap, sl, null, cons.getDtCons()));
                    }
                }
            }

            return errors;
        });
    }
}
