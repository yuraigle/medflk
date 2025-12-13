package ru.irkoms.medflk.q015;

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
public class Check_001F_00_0040 extends AbstractCheck {

    private final F002Service f002Service;

    @Override
    public String getErrorMessage() {
        return "Код СМО не найден в справочнике F002";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        LocalDate d1 = zlList.getSchet().getDschet();
        String codePlat = zlList.getSchet().getPlat();

        if (codePlat == null || d1 == null) return List.of(); // npe-safe

        if (!f002Service.isValidRegionalSmoOnDate(codePlat, d1)) {
            return List.of(new FlkP.Pr(null, null, codePlat));
        }

        return List.of();
    }
}
