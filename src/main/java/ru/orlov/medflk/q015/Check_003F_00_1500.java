package ru.orlov.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_1500 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сведения о препарате LEK_PR должны отсутствовать при типе услуги не 2 и 4";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            Integer uslTip = onkUsl.getUslTip();
            if (uslTip == null) return List.of();

            boolean hasLekPr = onkUsl.getLekPrList() != null && !onkUsl.getLekPrList().isEmpty();
            if (!List.of(2, 4).contains(uslTip) && hasLekPr) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
