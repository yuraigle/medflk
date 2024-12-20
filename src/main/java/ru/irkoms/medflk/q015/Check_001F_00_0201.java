package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.F032Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

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
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull String lpu = zap.getZSl().getLpu();
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();

            if (!f032Service.isValidRegionalCodeMoOnDate(lpu, d1)) {
                return List.of(new FlkP.Pr(zap, null, lpu));
            }

            return List.of();
        });
    }
}
