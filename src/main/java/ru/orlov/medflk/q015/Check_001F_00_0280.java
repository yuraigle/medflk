package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V020Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0280 extends AbstractCheck {

    private final V020Service v020Service;

    @Override
    public String getErrorMessage() {
        return "Код профиля койки PROFIL_K не найден в справочнике V020";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer profilK = sl.getProfilK();

            if (profilK != null && !v020Service.isValidProfilKOnDate(profilK, d2)) {
                return List.of(new FlkErr(zap, sl, null, profilK));
            }

            return List.of();
        });
    }
}
