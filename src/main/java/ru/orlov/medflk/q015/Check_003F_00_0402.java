package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class Check_003F_00_0402 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Дата направления обязательна при указании кода направляющей МО";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            String nprMo = zap.getZSl().getNprMo();
            LocalDate nprDate = zap.getZSl().getNprDate();

            if (nprDate == null && isNotBlank(nprMo)) {
                return List.of(new FlkErr(zap, null, null, null));
            }

            return List.of();
        });
    }
}
