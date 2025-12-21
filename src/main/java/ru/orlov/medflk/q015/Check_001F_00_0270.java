package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V002Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0270 extends AbstractCheck {

    private final V002Service v002Service;

    @Override
    public String getErrorMessage() {
        return "Код профиля МП должен быть найден в справочнике V002";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer profil = sl.getProfil();

            if (profil != null && !v002Service.isValidProfilOnDate(profil, d2)) {
                return List.of(new FlkErr(zap, sl, null, profil));
            }

            return List.of();
        });
    }
}
