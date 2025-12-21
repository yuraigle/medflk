package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.O002Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Pers;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0730 extends AbstractCheck {

    private final O002Service o002Service;

    @Override
    public String getErrorMessage() {
        return "Код места жительства должен быть найден в справочнике O002";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Pers pers = getPersById(zap.getPacient().getIdPac());
            String okatoG = pers.getOkatoG();

            if (okatoG != null && !o002Service.isValidOkato(okatoG)) {
                return List.of(new FlkErr(pers, okatoG));
            }

            return List.of();
        });
    }
}
