package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0170 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак внутрибольничного перевода VB_P должен быть пустым или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vbP = zap.getZSl().getVbP();

            if (vbP != null && vbP != 1) {
                return List.of(new FlkErr(zap, null, null, vbP));
            }

            return List.of();
        });
    }
}
