package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V020Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.Naz;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0340 extends AbstractCheck {

    private final V020Service v020Service;

    @Override
    public String getErrorMessage() {
        return "Профиль койки в назначении NAZ_PK не найден в справочнике V020";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkP.Pr> errors = new ArrayList<>();

            if (sl.getNazList() != null) {
                for (Naz naz : sl.getNazList()) {
                    Integer pk = naz.getNazPk();
                    if (pk != null && !v020Service.isValidProfilKOnDate(pk, d2)) {
                        errors.add(new FlkP.Pr(zap, sl, pk));
                    }
                }
            }

            return errors;
        });
    }
}
