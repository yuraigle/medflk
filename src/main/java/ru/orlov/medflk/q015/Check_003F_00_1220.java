package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Napr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1220 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код услуги NAPR_USL должен отсутствовать, если нет метода исследования";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNaprList() == null) return List.of();

            for (Napr napr : sl.getNaprList()) {
                String usl = napr.getNaprUsl();
                Integer met = napr.getMetIssl();
                if (met == null && usl != null) {
                    return List.of(new FlkErr(zap, sl, null, usl));
                }
            }

            return List.of();
        });
    }
}
