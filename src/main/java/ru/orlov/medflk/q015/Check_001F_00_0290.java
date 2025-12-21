package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V025Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0290 extends AbstractCheck {

    private final V025Service v025Service;

    @Override
    public String getErrorMessage() {
        return "Цель посещения должна быть найдена в справочнике V025";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            String pCel = sl.getPCel();

            if (pCel != null && !v025Service.isValidPCelOnDate(pCel, d2)) {
                return List.of(new FlkErr(zap, sl, null, pCel));
            }

            return List.of();
        });
    }
}
