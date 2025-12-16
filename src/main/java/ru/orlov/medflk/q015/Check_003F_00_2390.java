package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_2390 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (zlList1, zap, sl) -> {
            String slId = sl.getSlId();

            if (slId == null) {
                return List.of(new FlkP.Pr(zap, sl, null)); // не заполнен
            }

            boolean isUnique = zap.getZSl().getSlList().stream()
                    .filter(s -> Objects.equals(s.getSlId(), slId))
                    .count() == 1;

            if (!isUnique) {
                return List.of(new FlkP.Pr(zap, sl, slId)); // не уникальный
            }

            return List.of();
        });
    }
}
