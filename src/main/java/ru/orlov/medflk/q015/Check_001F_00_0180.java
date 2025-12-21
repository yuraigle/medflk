package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V014Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0180 extends AbstractCheck {

    private final V014Service v014Service;

    @Override
    public String getErrorMessage() {
        return "Код формы оказания МП должен быть найден в справочнике V014";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer forPom = zap.getZSl().getForPom();

            if (forPom != null && !v014Service.isValidForPomOnDate(forPom, d2)) {
                return List.of(new FlkErr(zap, null, null, forPom));
            }

            return List.of();
        });
    }
}
