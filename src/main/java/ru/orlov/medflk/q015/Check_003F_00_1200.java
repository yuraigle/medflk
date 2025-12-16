package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Napr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1200 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNaprList() == null) return List.of();

            for (Napr napr : sl.getNaprList()) {
                Integer naprV = napr.getNaprV();
                if (!Objects.equals(3, naprV) && napr.getMetIssl() != null) {
                    return List.of(new FlkErr(zap, sl, null, napr.getMetIssl()));
                }
            }

            return List.of();
        });
    }
}
