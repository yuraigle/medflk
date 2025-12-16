package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_003F_00_1150 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getKsgKpg() == null) return List.of();

            boolean hasSlKoef = sl.getKsgKpg().getSlKoefList() != null
                    && !sl.getKsgKpg().getSlKoefList().isEmpty();

            BigDecimal itSl = sl.getKsgKpg().getItSl();
            if (!hasSlKoef && itSl != null) {
                return List.of(new FlkP.Pr(zap, sl, itSl));
            }

            return List.of();
        });
    }
}
