package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Pers;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_1540 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Фамилия должна отсутствовать при NOVOR<>0 или DOST=2";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Pers pers = getPersById(zap.getPacient().getIdPac());
            String novor = zap.getPacient().getNovor();
            boolean hasDost2 = pers.getDostList() != null && pers.getDostList().contains(2);

            if ((!"0".equals(novor) || hasDost2) && !isBlank(pers.getFam())) {
                return List.of(new FlkErr(zap, null, null, pers.getFam()));
            }

            return List.of();
        });
    }
}
