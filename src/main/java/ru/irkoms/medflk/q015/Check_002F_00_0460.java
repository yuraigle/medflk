package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_002F_00_0460 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        Integer yr = zlList.getSchet().getYear();
        if (yr == null) return List.of();
        String year = yr.toString();

        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getKsgKpg() == null) return List.of();

            if (!year.equals(sl.getKsgKpg().getVerKsg())) {
                return List.of(new FlkP.Pr(zap, sl, sl.getKsgKpg().getVerKsg()));
            }

            return List.of();
        });
    }
}
