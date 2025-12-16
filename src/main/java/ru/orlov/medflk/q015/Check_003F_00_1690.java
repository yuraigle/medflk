package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_1690 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "ДР законного представителя должна быть указана при мед. помощи новорожденному";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull PersList.Pers pers = getPersById(zap.getPacient().getIdPac());
            String novor = zap.getPacient().getNovor();

            if (!"0".equals(novor) && pers.getDrP() == null) {
                return List.of(new FlkP.Pr(pers, null));
            }

            return List.of();
        });
    }
}
