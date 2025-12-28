package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N017Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0550 extends AbstractCheck {

    private final N017Service n017Service;

    @Override
    public String getErrorMessage() {
        return "Тип лучевой терапии LUCH_TIP должен быть найден в справочнике N017";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer tip = onkUsl.getLuchTip();

            if (tip != null && !n017Service.isValidTipOnDate(tip, d2)) {
                return List.of(new FlkErr(zap, sl, null, tip));
            }

            return List.of();
        });
    }
}
