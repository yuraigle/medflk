package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V014Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0180 extends AbstractCheck {

    private final V014Service v014Service;

    @Override
    public String getErrorMessage() {
        return "Код формы оказания МП не найден в справочнике V014";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Integer forPom = zap.getZSl().getForPom();
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();

            if (!v014Service.isValidForPomOnDate(forPom, d1)) {
                return List.of(new FlkP.Pr(zap, null, forPom));
            }

            return List.of();
        });
    }
}
