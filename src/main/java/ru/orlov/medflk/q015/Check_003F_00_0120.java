package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0120 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "СМО должна быть заполнена при указании плательщика";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        if (isBlank(zlList.getSchet().getPlat())) return List.of();

        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getPacient().getSmo() == null) {
                return List.of(new FlkErr(zap, null, null, null));
            }
            return List.of();
        });
    }

}
