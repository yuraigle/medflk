package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V027Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0300 extends AbstractCheck {

    private final V027Service v027Service;

    @Override
    public String getErrorMessage() {
        return "Характер заболевания C_ZAB не найден в справочнике V027";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer cZab = sl.getCZab();

            if (cZab != null && !v027Service.isValidCZabOnDate(cZab, d2)) {
                return List.of(new FlkP.Pr(zap, sl, cZab));
            }

            return List.of();
        });
    }
}
