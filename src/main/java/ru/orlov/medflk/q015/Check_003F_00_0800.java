package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_0800 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сведения о КСГ/КПГ не заполнены при способе оплаты 33";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            Integer idsp = zap.getZSl().getIdsp();

            if (Objects.equals(33, idsp) && sl.getKsgKpg() == null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
