package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V008Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0170 extends AbstractCheck {

    private final V008Service v008Service;

    @Override
    public String getErrorMessage() {
        return "Код вида МП должен быть найден в справочнике V008";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer vidPom = zap.getZSl().getVidpom();

            if (vidPom != null && !v008Service.isValidVidPomOnDate(vidPom, d2)) {
                return List.of(new FlkErr(zap, null, null, vidPom));
            }

            return List.of();
        });
    }
}
