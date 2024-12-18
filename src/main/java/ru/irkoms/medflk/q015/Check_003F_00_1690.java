package ru.irkoms.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZap;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.List;

import static ru.irkoms.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_1690 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "ДР законного представителя должна быть указана при мед. помощи новорожденному";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull APers pers = getPersById(zap.getPacient().getIdPac());
            String novor = zap.getPacient().getNovor();

            if (!"0".equals(novor) && pers.getDrP() == null) {
                return List.of(new FlkP.Pr(pers, null));
            }

            return List.of();
        });
    }
}
