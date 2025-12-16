package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_1740 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Номер документа должен быть указан при типе полиса не ПЕО";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull PersList.Pers pers = getPersById(zap.getPacient().getIdPac());
            Integer vPolis = zap.getPacient().getVpolis();

            if (!Objects.equals(3, vPolis) && pers.getDocNum() == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }
}
