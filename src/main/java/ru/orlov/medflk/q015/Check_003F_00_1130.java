package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1130 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "N_KPG должен отсутствовать при заполненном N_KSG";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getKsgKpg() == null) return List.of();

            String nKpg = sl.getKsgKpg().getNKpg();
            String nKsg = sl.getKsgKpg().getNKsg();

            if (nKpg != null && nKsg != null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
