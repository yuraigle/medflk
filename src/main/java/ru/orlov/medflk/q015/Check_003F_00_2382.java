package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_2382 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer pOtk = zap.getZSl().getPOtk();
            Integer rsltD = zap.getZSl().getRsltD();

            if (Objects.equals(pOtk, 1) && rsltD != null) {
                return List.of(new FlkErr(zap, null, null, rsltD));
            }

            return List.of();
        });
    }
}
