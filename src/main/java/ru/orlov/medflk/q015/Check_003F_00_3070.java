package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_3070 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Персона не найдена в L-файле";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            PersList.Pers pers = getPersById(zap.getPacient().getIdPac());

            if (pers == null) {
                return List.of(new FlkErr(zap, null, null, null));
            }

            return List.of();
        });
    }

}
