package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_2850 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            String idServ = usl.getIdserv();
            if (idServ == null) {
                return List.of(new FlkP.Pr(zap, sl, usl, null)); // пустой
            } else {
                boolean isUnique = sl.getUslList().stream()
                        .filter(u -> Objects.equals(u.getIdserv(), idServ))
                        .count() == 1;

                if (!isUnique) {
                    return List.of(new FlkP.Pr(zap, sl, usl, idServ)); // не уникальный
                }
            }

            return List.of();
        });
    }
}
