package ru.irkoms.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static ru.irkoms.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_1530 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Фамилия должна быть заполнена";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull APers pers = getPersById(zap.getPacient().getIdPac());
            String novor = zap.getPacient().getNovor();
            boolean hasDost2 = pers.getDostList() != null && pers.getDostList().contains(2);

            if ("0".equals(novor) && !hasDost2 && isEmpty(pers.getFam())) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }
}
