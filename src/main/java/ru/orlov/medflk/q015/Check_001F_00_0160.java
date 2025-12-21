package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V006Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0160 extends AbstractCheck {

    private final V006Service v006Service;

    @Override
    public String getErrorMessage() {
        return "Код условия оказания МП должен быть найден в справочнике V006";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer uslOk = zap.getZSl().getUslOk();

            if (uslOk != null && !v006Service.isValidUslOkOnDate(uslOk, d2)) {
                return List.of(new FlkErr(zap, null, null, uslOk));
            }

            return List.of();
        });
    }
}
