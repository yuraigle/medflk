package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_2382 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer pOtk = zap.getZSl().getPOtk();
            Integer rsltD = zap.getZSl().getRsltD();

            if (Objects.equals(pOtk, 1) && rsltD != null) {
                return List.of(new FlkP.Pr(zap, null, rsltD));
            }

            return List.of();
        });
    }
}
