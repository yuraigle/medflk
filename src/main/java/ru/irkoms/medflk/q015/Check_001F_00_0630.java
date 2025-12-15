package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.F032Service;
import ru.irkoms.medflk.domain.V002Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0630 extends AbstractCheck {

    private final V002Service v002Service;

    @Override
    public String getErrorMessage() {
        return "Профиль МП в услуге не найден в справочнике V002";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer pmp = usl.getProfil();

            if (pmp != null && !v002Service.isValidProfilOnDate(pmp, d2)) {
                return List.of(new FlkP.Pr(zap, sl, usl, pmp));
            }

            return List.of();
        });
    }
}
