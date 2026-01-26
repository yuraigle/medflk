package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1100 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "При IDSP=33 и отсутствии N_KPG должен быть указан N_KSG";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getKsgKpg() == null) return List.of();

            Integer idsp = zap.getZSl().getIdsp();
            String nKpg = sl.getKsgKpg().getNKpg();
            String nKsg = sl.getKsgKpg().getNKsg();

            if (idsp == 33 && nKpg == null && nKsg == null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
