package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.Utils.dsIsOnkoC00ToD10OrD45ToD48;

@Component
public class Check_003F_00_0872 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Нет сведений об онкологии при онко-диагнозе в файле T";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {

            if (dsIsOnkoC00ToD10OrD45ToD48(sl.getDs1()) && sl.getOnkSl() == null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
