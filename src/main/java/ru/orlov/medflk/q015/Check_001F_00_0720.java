package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.F011Service;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0720 extends AbstractCheck {

    private final F011Service f011Service;

    @Override
    public String getErrorMessage() {
        return "Тип документа не найден в справочнике F011";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull PersList.Pers pers = getPersById(zap.getPacient().getIdPac());
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            Integer docType = pers.getDocType();

            if (docType != null && !f011Service.isValidIdDocOnDate(docType, d1)) {
                return List.of(new FlkP.Pr(pers, docType));
            }

            return List.of();
        });
    }
}
