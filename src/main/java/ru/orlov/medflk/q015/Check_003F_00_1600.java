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
public class Check_003F_00_1600 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Имя представителя должно отсутствовать при NOVOR=0 или DOST_P=3";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Pers pers = getPersById(zap.getPacient().getIdPac());
            String novor = zap.getPacient().getNovor();
            boolean hasDost3 = pers.getDostPList() != null && pers.getDostPList().contains(3);

            if (("0".equals(novor) || hasDost3) && pers.getImP() != null) {
                return List.of(new FlkErr(zap, null, null, pers.getImP()));
            }

            return List.of();
        });
    }
}
