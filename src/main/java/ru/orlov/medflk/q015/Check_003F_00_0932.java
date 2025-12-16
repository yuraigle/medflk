package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0932 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Тариф онко-случая не заполнен";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (zlList1, zap, sl) -> {
            String ds1 = sl.getDs1();
            if (ds1 == null) return List.of();

            if (dsIsOnkoC00ToD10OrD45ToD48(ds1) && sl.getTarif() == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
