package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V006Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0160 extends AbstractCheck {

    private final V006Service v006Service;

    @Override
    public String getErrorMessage() {
        return "Код условия оказания МП не найден в справочнике V006";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer uslOk = zap.getZSl().getUslOk();

            if (uslOk != null && !v006Service.isValidUslOkOnDate(uslOk, d2)) {
                return List.of(new FlkP.Pr(zap, null, uslOk));
            }

            return List.of();
        });
    }
}
