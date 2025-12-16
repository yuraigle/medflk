package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.O002Service;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0740 extends AbstractCheck {

    private final O002Service o002Service;

    @Override
    public String getErrorMessage() {
        return "Код места пребывания не найден в справочнике O002";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull PersList.Pers pers = getPersById(zap.getPacient().getIdPac());
            String okatoP = pers.getOkatoP();

            if (okatoP != null && !o002Service.isValidOkato(okatoP)) {
                return List.of(new FlkP.Pr(pers, okatoP));
            }

            return List.of();
        });
    }
}
