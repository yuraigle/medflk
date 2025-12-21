package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V017Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0240 extends AbstractCheck {

    private final V017Service v017Service;

    @Override
    public String getErrorMessage() {
        return "Результат диспансеризации RSLT_D должен быть найден в справочнике V017";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            @NonNull Integer rsltD = zap.getZSl().getRsltD();

            if (rsltD != null && !v017Service.isValidRsltDOnDate(rsltD, d1)) {
                return List.of(new FlkErr(zap, null, null, rsltD));
            }

            return List.of();
        });
    }
}
