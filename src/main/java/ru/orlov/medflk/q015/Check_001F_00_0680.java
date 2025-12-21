package ru.orlov.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.F014Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.Sank;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0680 extends AbstractCheck {

    private final F014Service f014Service;

    @Override
    public String getErrorMessage() {
        return "Код причины отказа должен быть найден в справочнике F014";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {

            // Вообще, в счёте от МО не должно быть санкций
            if (zap.getZSl().getSankList() == null) {
                return List.of();
            }

            List<FlkErr> errors = new ArrayList<>();
            for (Sank sank : zap.getZSl().getSankList()) {
                LocalDate d1 = sank.getDateAct();
                Integer sOsn = sank.getSOsn();
                if (d1 != null && sOsn != null && !f014Service.isValidSOsnOnDate(sOsn, d1)) {
                    errors.add(new FlkErr(zap, null, null, sOsn));
                }
            }

            return errors;
        });
    }
}
