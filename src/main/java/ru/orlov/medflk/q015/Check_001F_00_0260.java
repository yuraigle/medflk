package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V019Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0260 extends AbstractCheck {

    private final V019Service v019Service;

    @Override
    public String getErrorMessage() {
        return "Метод ВМП METOD_HMP не найден в справочнике V019";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            Integer met = sl.getMetodHmp();

            if (met != null && !v019Service.isValidMetOnDate(met, d1)) {
                return List.of(new FlkErr(zap, sl, null, met));
            }

            return List.of();
        });
    }
}
