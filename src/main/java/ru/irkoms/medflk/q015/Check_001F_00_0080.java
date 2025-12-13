package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.F002Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0080 extends AbstractCheck {

    private final F002Service f002Service;

    @Override
    public String getErrorMessage() {
        return "Реестровый номер СМО не найден в справочнике F002";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            String smo = zap.getPacient().getSmo();

            if (smo != null && !f002Service.isValidSmoOnDate(smo, d2)) {
                return List.of(new FlkP.Pr(zap, null, smo)); // err
            }

            return List.of(); // ok
        });
    }
}
