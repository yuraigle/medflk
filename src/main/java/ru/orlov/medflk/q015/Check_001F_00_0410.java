package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N018Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0410 extends AbstractCheck {

    private final N018Service n018Service;

    @Override
    public String getErrorMessage() {
        return "Повод обращения DS1_T должен быть найден в справочнике N018";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer ds1T = onkSl.getDs1T();

            if (ds1T != null && !n018Service.isValidReasonOnDate(ds1T, d2)) {
                return List.of(new FlkErr(zap, sl, null, ds1T));
            }

            return List.of();
        });
    }
}
