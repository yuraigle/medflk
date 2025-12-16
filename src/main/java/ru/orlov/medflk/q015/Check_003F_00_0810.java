package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0810 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сведения о КСГ/КПГ заполнены при способе оплаты не 33";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer idsp = zap.getZSl().getIdsp();

            if (idsp != 33 && sl.getKsgKpg() != null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
