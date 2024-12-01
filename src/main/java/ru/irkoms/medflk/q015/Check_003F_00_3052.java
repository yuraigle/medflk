package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ANaz;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_003F_00_3052 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (ANaz naz : sl.getNazList()) {
                Integer nazR = naz.getNazR();
                Integer rsltD = zap.getZSl().getRsltD();

                if (rsltD != null && List.of(3, 4, 5, 31, 32, 37).contains(rsltD) && nazR == null) {
                    return List.of(new FlkP.Pr(zap, sl, null));
                }
            }

            return List.of();
        });
    }
}
