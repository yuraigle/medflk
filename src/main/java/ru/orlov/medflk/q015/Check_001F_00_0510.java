package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N013Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0510 extends AbstractCheck {

    private final N013Service n013Service;

    @Override
    public String getErrorMessage() {
        return "Тип онко-услуги USL_TIP должен быть найден в справочнике N013";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            Integer tip = onkUsl.getUslTip();

            if (tip != null && !n013Service.isValidTipOnDate(tip, d2)) {
                return List.of(new FlkErr(zap, sl, null, tip));
            }

            return List.of();
        });
    }
}
