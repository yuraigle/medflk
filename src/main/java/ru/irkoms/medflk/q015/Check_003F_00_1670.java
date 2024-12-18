package ru.irkoms.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static ru.irkoms.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_1670 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Пол законного представителя должен быть указан при мед. помощи новорожденному";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull APers pers = getPersById(zap.getPacient().getIdPac());
            String novor = zap.getPacient().getNovor();
            Integer wP = pers.getWP();

            if (!"0".equals(novor) && wP == null) {
                return List.of(new FlkP.Pr(pers, null));
            }

            return List.of();
        });
    }
}
