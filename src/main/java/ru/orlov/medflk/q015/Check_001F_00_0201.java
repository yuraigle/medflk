package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.F032Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0201 extends AbstractCheck {

    private final F032Service f032Service;

    @Override
    public String getErrorMessage() {
        return "Код ЛПУ не найден в справочнике F032 для региона";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull String lpu = zap.getZSl().getLpu();
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();

            if (!f032Service.isValidRegionalCodeMoOnDate(lpu, d1)) {
                return List.of(new FlkErr(zap, null, null, lpu));
            }

            return List.of();
        });
    }
}
