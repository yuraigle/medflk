package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0181 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Тип оплаты должен быть (0,1,2,3)";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer oplata = zap.getZSl().getOplata();

            if (oplata != null && !List.of(0, 1, 2, 3).contains(oplata)) {
                return List.of(new FlkErr(zap, null, null, oplata));
            }

            return List.of();
        });
    }
}
