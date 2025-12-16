package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.F032Service;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0621 extends AbstractCheck {

    private final F032Service f032Service;

    @Override
    public String getErrorMessage() {
        return "Код МО в услуге не найден в справочнике F032";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            String lpu = usl.getLpu();

            if (lpu != null && !f032Service.isValidRegionalCodeMoOnDate(lpu, d2)) {
                return List.of(new FlkP.Pr(zap, sl, lpu));
            }

            return List.of();
        });
    }
}
