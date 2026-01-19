package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_002F_00_0465 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Коэффициент достижения зп K_ZP должен быть не меньше 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getKsgKpg() == null) return List.of();

            BigDecimal kZp = sl.getKsgKpg().getKZp();
            if (kZp != null && kZp.compareTo(BigDecimal.ONE) < 0) {
                return List.of(new FlkErr(zap, sl, null, kZp));
            }

            return List.of();
        });
    }
}
