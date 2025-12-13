package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V020Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

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
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer profilK = sl.getProfilK();

            if (profilK != null && !v020Service.isValidProfilKOnDate(profilK, d2)) {
                return List.of(new FlkP.Pr(zap, sl, profilK));
            }

            return List.of();
        });
    }
}
