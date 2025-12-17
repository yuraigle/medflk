package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Pers;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_1680 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Пол законного представителя должен отсутствовать при NOVOR=0";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Pers pers = getPersById(zap.getPacient().getIdPac());
            String novor = zap.getPacient().getNovor();

            if ("0".equals(novor) && pers.getWP() != null) {
                return List.of(new FlkErr(pers, pers.getWP()));
            }

            return List.of();
        });
    }
}
