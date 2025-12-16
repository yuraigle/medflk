package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V024Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0610 extends AbstractCheck {

    private final V024Service v024Service;

    @Override
    public String getErrorMessage() {
        return "Классификационный критерий не найден в справочнике V024";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (sl.getKsgKpg() == null) {
                return errors;
            }

            if (sl.getKsgKpg().getCritList() != null) {
                for (String crit : sl.getKsgKpg().getCritList()) {
                    if (crit != null && !v024Service.isValidCritOnDate(crit, d2)) {
                        errors.add(new FlkErr(zap, sl, null, crit));
                    }
                }
            }

            return errors;
        });
    }
}
