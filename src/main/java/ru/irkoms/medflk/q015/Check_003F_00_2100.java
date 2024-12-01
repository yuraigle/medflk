package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_2100 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {

        // по федеральному формату дают тэг внутри SCHET
        if (zlList.getSchet().getDisp() != null) {
            return List.of(); // ok
        }

        // сейчас по региональному формату тэг ставят внутри SL
        return iterateOverSl(zlList, persList, (a, z, sl) -> {
            if (sl.getDisp() == null) {
                return List.of(new FlkP.Pr(z, sl, null));
            }
            return List.of();
        });
    }

}
