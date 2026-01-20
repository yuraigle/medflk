package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0581 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Санкции должны присутствовать при заполненной сумме SANK_IT";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            boolean hasSank = zap.getZSl().getSankList() != null
                              && !zap.getZSl().getSankList().isEmpty();

            if (zap.getZSl().getSankIt() != null && !hasSank) {
                return List.of(new FlkErr(zap, null, null, null));
            }

            return List.of();
        });
    }

}
