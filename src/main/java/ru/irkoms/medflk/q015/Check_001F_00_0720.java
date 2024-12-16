package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.F011Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.List;

import static ru.irkoms.medflk.service.Q015ValidationService.getPersById;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0720 extends AbstractCheck {

    private final F011Service f011Service;

    @Override
    public String getErrorMessage() {
        return "Тип документа не найден в справочнике F011";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull APers pers = getPersById(zap.getPacient().getIdPac());
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            Integer docType = pers.getDocType();

            if (docType != null && !f011Service.isValidIdDocOnDate(docType, d1)) {
                return List.of(new FlkP.Pr(pers, docType));
            }

            return List.of();
        });
    }
}
