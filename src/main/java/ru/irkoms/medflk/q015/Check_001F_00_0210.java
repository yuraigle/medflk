package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V009Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0210 extends AbstractCheck {

    private final V009Service v009Service;

    @Override
    public String getErrorMessage() {
        return "Результат обращения не найден в справочнике V009 для условий оказания";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Integer rslt = zap.getZSl().getRslt();
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();

            if (!v009Service.isValidResultOnUslOkAndDate(rslt, uslOk, d1)) {
                return List.of(new FlkP.Pr(zap, null, rslt));
            }

            return List.of();
        });
    }
}
