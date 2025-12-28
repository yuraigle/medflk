package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N016Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0540 extends AbstractCheck {

    private final N016Service n016Service;

    @Override
    public String getErrorMessage() {
        return "Цикл лекарственной терапии LEK_TIP_V должен быть найден в справочнике N016";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer tip = onkUsl.getLekTipV();

            if (tip != null && !n016Service.isValidTipOnDate(tip, d2)) {
                return List.of(new FlkErr(zap, sl, null, tip));
            }

            return List.of();
        });
    }
}
