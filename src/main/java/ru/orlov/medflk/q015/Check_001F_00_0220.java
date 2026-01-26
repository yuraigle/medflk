package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V012Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0220 extends AbstractCheck {

    private final V012Service v012Service;

    @Override
    public String getErrorMessage() {
        return "Исход заболевания должен быть в справочнике V012";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            Integer ishod = zap.getZSl().getIshod();

            if (ishod != null && !v012Service.isValidIshodOnUslOkAndDate(ishod, uslOk, d1)) {
                return List.of(new FlkErr(zap, null, null, ishod));
            }

            return List.of();
        });
    }
}
