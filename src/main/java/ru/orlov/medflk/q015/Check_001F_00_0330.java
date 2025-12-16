package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.V002Service;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0330 extends AbstractCheck {

    private final V002Service v002Service;

    @Override
    public String getErrorMessage() {
        return "Профиль медицинской помощи в назначении NAZ_PMP не найден в справочнике V002";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkP.Pr> errors = new ArrayList<>();

            if (sl.getNazList() != null) {
                for (Naz naz : sl.getNazList()) {
                    Integer pmp = naz.getNazPmp();
                    if (pmp != null && !v002Service.isValidProfilOnDate(pmp, d2)) {
                        errors.add(new FlkP.Pr(zap, sl, pmp));
                    }
                }
            }

            return errors;
        });
    }
}
