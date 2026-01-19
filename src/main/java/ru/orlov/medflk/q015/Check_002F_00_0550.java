package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Pers;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_002F_00_0550 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код достоверности DOST_P должен быть пустым или 1-6";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull Pers pers = getPersById(zap.getPacient().getIdPac());

            List<FlkErr> errors = new ArrayList<>();

            if (pers.getDostPList() != null) {
                for (Integer dostP : pers.getDostPList()) {
                    if (dostP != null && !List.of(1, 2, 3, 4, 5, 6).contains(dostP)) {
                        errors.add(new FlkErr(pers, dostP));
                    }
                }
            }

            return errors;
        });
    }
}
