package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V029Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0350 extends AbstractCheck {

    private final V029Service v029Service;

    @Override
    public String getErrorMessage() {
        return "Метод диагностического исследования NAZ_V не найден в справочнике V029";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (sl.getNazList() != null) {
                for (Naz naz : sl.getNazList()) {
                    Integer met = naz.getNazV();
                    if (met != null && !v029Service.isValidIdMetOnDate(met, d2)) {
                        errors.add(new FlkErr(zap, sl, null, met));
                    }
                }
            }

            return errors;
        });
    }
}
