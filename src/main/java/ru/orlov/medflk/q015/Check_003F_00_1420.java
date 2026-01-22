package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1420 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Тип хирургического лечения HIR_TIP должен отсутствовать при типе услуги не 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            Integer hirTip = onkUsl.getHirTip();
            Integer uslTip = onkUsl.getUslTip();

            if (!Objects.equals(1, uslTip) && hirTip != null) {
                return List.of(new FlkErr(zap, sl, null, hirTip));
            }

            return List.of();
        });
    }
}
