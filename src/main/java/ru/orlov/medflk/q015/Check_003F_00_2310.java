package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_2310 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer idsp = zap.getZSl().getIdsp();
            if (idsp == null) { // todo: Hibernate?
                return List.of(new FlkErr(zap, null, null, null));
            }

            return List.of();
        });
    }
}
