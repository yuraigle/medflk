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
public class Check_003F_00_1002 extends AbstractCheck {

    private final M002Service m002Service;

    @Override
    public String getErrorMessage() {
        return "Значение Nodus должно отсутствовать при диагнозе не из МКБ-О";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            String ds1 = sl.getDs1();

            if (ds1 == null) return List.of(); // npe-safe

            String dsO = m002Service.findMkbOTopography(ds1);
            Integer onkN = onkSl.getOnkN();
            if (dsO == null && onkN != null) {
                return List.of(new FlkErr(zap, sl, null, onkN));
            }

            return List.of();
        });
    }

}
