package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V009Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0210 extends AbstractCheck {

    private final V009Service v009Service;

    @Override
    public String getErrorMessage() {
        return "Результат обращения должен быть в справочнике V009";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            @NonNull Integer rslt = zap.getZSl().getRslt();

            if (!v009Service.isValidResultOnUslOkAndDate(rslt, uslOk, d1)) {
                return List.of(new FlkErr(zap, null, null, rslt));
            }

            return List.of();
        });
    }
}
