package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.F032Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0361 extends AbstractCheck {

    private final F032Service f032Service;

    @Override
    public String getErrorMessage() {
        return "Код МО в направлении NAPR_MO не найден в справочнике F032";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (sl.getNazList() != null) {
                for (Naz naz : sl.getNazList()) {
                    String lpu = naz.getNaprMo();
                    if (lpu != null && !f032Service.isValidCodeMoOnDate(lpu, d2)) {
                        errors.add(new FlkErr(zap, sl, null, lpu));
                    }
                }
            }

            return errors;
        });
    }
}
