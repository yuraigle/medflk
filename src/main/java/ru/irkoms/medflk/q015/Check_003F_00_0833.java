package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

import static ru.irkoms.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0833 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сведения о консилиуме не указаны при онко-диагнозе или подозрении на онко";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer dsOnk = sl.getDsOnk();
            boolean hasCons = sl.getConsList() != null && !sl.getConsList().isEmpty();

            if ((Objects.equals(1, dsOnk) || dsIsOnkoC00ToD10OrD45ToD48(sl.getDs1())) && !hasCons) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
