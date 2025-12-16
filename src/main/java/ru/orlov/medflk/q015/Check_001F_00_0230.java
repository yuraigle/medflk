package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V010Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0230 extends AbstractCheck {

    private final V010Service v010Service;

    @Override
    public String getErrorMessage() {
        return "Идентификатор способа оплаты IDSP не найден в справочнике V010";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            Integer idsp = zap.getZSl().getIdsp();

            if (idsp != null && !v010Service.isValidIdspOnDate(idsp, d1)) {
                return List.of(new FlkErr(zap, null, null, idsp));
            }

            return List.of();
        });
    }
}
