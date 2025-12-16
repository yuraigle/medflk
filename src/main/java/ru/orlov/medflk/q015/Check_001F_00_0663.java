package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V021Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.MrUslN;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0663 extends AbstractCheck {

    private final V021Service v021Service;

    @Override
    public String getErrorMessage() {
        return "Специальность медработника в услуге не найдена в справочнике V021";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (usl.getMrUslNList() != null) {
                for (MrUslN mrUslN : usl.getMrUslNList()) {
                    Integer prvs = mrUslN.getPrvs();
                    if (prvs != null && !v021Service.isValidPrvsOnDate(prvs, d2)) {
                        errors.add(new FlkErr(zap, sl, usl, prvs));
                    }
                }
            }

            return errors;
        });
    }
}
