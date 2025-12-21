package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.F002Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0090 extends AbstractCheck {

    private final F002Service f002Service;

    @Override
    public String getErrorMessage() {
        return "ОГРН СМО должен быть найден в справочнике F002";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            String smo = zap.getPacient().getSmo();
            String ogrn = zap.getPacient().getSmoOgrn();

            if (ogrn != null && !f002Service.isValidSmoOgrnOnDate(smo, ogrn, d2)) {
                return List.of(new FlkErr(zap, null, null, ogrn));
            }

            return List.of(); // ok
        });
    }
}
