package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V008Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0170 extends AbstractCheck {

    private final V008Service v008Service;

    @Override
    public String getErrorMessage() {
        return "Код вида МП не найден в справочнике V008";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Integer vidPom = zap.getZSl().getVidpom();
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();

            if (!v008Service.isValidVidPomOnDate(vidPom, d1)) {
                return List.of(new FlkP.Pr(zap, null, vidPom));
            }

            return List.of();
        });
    }
}
