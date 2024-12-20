package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V006Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

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
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();

            if (!v006Service.isValidUslOkOnDate(uslOk, d1)) {
                return List.of(new FlkP.Pr(zap, null, uslOk));
            }

            return List.of();
        });
    }
}
