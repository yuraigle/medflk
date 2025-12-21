package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.F008Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0060 extends AbstractCheck {

    private final F008Service f008Service;

    @Override
    public String getErrorMessage() {
        return "Тип ДПФС должен быть найден в справочнике F008";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer vpolis = zap.getPacient().getVpolis();

            if (vpolis != null && !f008Service.isValidVpolisOnDate(vpolis, d2)) {
                return List.of(new FlkErr(zap, null, null, vpolis)); // err
            }

            return List.of(); // ok
        });
    }
}
