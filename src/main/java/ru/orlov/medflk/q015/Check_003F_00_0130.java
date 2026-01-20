package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0130 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "ОГРН СМО должен быть заполнен при отсутствии кода СМО";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
//        if (isBlank(zlList.getSchet().getPlat())) return List.of();

        return iterateOverZap(zlList, persList, (a, zap) -> {
            String smo = zap.getPacient().getSmo();
            String ogrn = zap.getPacient().getSmoOgrn();

            if (smo == null && isBlank(ogrn)) {
                return List.of(new FlkErr(zap, null, null, null));
            }
            return List.of();
        });
    }

}
