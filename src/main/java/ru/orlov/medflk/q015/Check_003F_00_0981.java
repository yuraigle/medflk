package ru.orlov.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.M002Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_0981 extends AbstractCheck {

    private final M002Service m002Service;

    @Override
    public String getErrorMessage() {
        return "Стадия заболевания STAD обязательна при диагнозе из МКБ-О";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            String ds1 = sl.getDs1();
            Integer ds1T = onkSl.getDs1T();

            if (ds1 == null || ds1T == null) return List.of(); // npe-safe

            String dsO = m002Service.findMkbOTopography(ds1);
            Integer stad = onkSl.getStad();
            if (List.of(0, 1, 2, 3, 4).contains(ds1T) && dsO != null && stad == null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }

}
