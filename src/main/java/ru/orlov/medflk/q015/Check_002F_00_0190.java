package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0190 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак мобильной бригады VBR должен быть 0 или 1";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vbr = zap.getZSl().getVbr();

            if (vbr != null && !List.of(0, 1).contains(vbr)) {
                return List.of(new FlkErr(zap, null, null, vbr));
            }

            return List.of();
        });
    }
}
