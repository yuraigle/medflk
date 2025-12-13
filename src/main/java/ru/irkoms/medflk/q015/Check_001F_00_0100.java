package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.F010Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0100 extends AbstractCheck {

    private final F010Service f010Service;

    @Override
    public String getErrorMessage() {
        return "Код ОКАТО территории страхования SMO_OK не найден в справочнике F010";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            String smoOk = zap.getPacient().getSmoOk();

            if (smoOk != null && !f010Service.isValidRegionOkatoOnDate(smoOk, d2)) {
                return List.of(new FlkP.Pr(zap, null, smoOk));
            }

            return List.of(); // ok
        });
    }
}
