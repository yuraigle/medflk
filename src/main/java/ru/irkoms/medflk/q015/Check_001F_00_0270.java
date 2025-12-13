package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V002Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0270 extends AbstractCheck {

    private final V002Service v002Service;

    @Override
    public String getErrorMessage() {
        return "Код профиля МП не найден в справочнике V002";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer profil = sl.getProfil();

            if (profil != null && !v002Service.isValidProfilOnDate(profil, d2)) {
                return List.of(new FlkP.Pr(zap, sl, profil));
            }

            return List.of();
        });
    }
}
