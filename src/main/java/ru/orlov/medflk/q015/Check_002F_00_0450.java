package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0450 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак использования КСЛП SL_K должен быть 0 или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getKsgKpg() == null) return List.of();

            Integer slK = sl.getKsgKpg().getSlK();
            if (slK != null && !List.of(0, 1).contains(slK)) {
                return List.of(new FlkErr(zap, sl, null, slK));
            }

            return List.of();
        });
    }
}
