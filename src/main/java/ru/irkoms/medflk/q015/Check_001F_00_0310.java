package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V021Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0310 extends AbstractCheck {

    private final V021Service v021Service;

    @Override
    public String getErrorMessage() {
        return "Специальность лечащего врача PRVS не найдена в справочнике V021";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer prvs = sl.getPrvs();

            if (prvs != null && !v021Service.isValidPrvsOnDate(prvs, d2)) {
                return List.of(new FlkP.Pr(zap, sl, prvs));
            }

            return List.of();
        });
    }
}
