package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0440 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак использования подгруппы КСГ KSG_PG должен быть 0 или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getKsgKpg() == null) return List.of();

            Integer ksgPg = sl.getKsgKpg().getKsgPg();
            if (ksgPg != null && !List.of(0, 1).contains(ksgPg)) {
                return List.of(new FlkErr(zap, sl, null, ksgPg));
            }

            return List.of();
        });
    }
}
