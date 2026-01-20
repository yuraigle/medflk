package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_003F_00_0591 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сумма санкций SANK_IT должна быть заполнена при наличии санкций";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            boolean hasSank = zap.getZSl().getSankList() != null && zap.getZSl().getSankList().stream()
                    .anyMatch(s -> s.getSSum() != null && s.getSSum().compareTo(BigDecimal.ZERO) != 0);

            if (hasSank && zap.getZSl().getSankIt() == null) {
                return List.of(new FlkErr(zap, null, null, null));
            }

            return List.of();
        });
    }

}
