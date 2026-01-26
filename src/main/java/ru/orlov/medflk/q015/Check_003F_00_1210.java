package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Napr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_1210 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код услуги NAPR_USL обязателен при заполненном методе исследования";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNaprList() == null) return List.of();

            for (Napr napr : sl.getNaprList()) {
                String usl = napr.getNaprUsl();
                Integer met = napr.getMetIssl();
                if (met != null && isBlank(usl)) {
                    return List.of(new FlkErr(zap, sl, null, usl));
                }
            }

            return List.of();
        });
    }
}
