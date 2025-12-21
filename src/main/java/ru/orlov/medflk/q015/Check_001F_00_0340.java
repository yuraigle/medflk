package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V020Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0340 extends AbstractCheck {

    private final V020Service v020Service;

    @Override
    public String getErrorMessage() {
        return "Профиль койки в назначении NAZ_PK должен быть найден в справочнике V020";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (sl.getNazList() != null) {
                for (Naz naz : sl.getNazList()) {
                    Integer pk = naz.getNazPk();
                    if (pk != null && !v020Service.isValidProfilKOnDate(pk, d2)) {
                        errors.add(new FlkErr(zap, sl, null, pk));
                    }
                }
            }

            return errors;
        });
    }
}
