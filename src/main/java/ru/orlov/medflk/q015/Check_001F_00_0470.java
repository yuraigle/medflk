package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N010Service;
import ru.orlov.medflk.jaxb.Bdiag;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0470 extends AbstractCheck {

    private final N010Service n010Service;

    @Override
    public String getErrorMessage() {
        return "Код диагностики DIAG_CODE должен быть найден в справочнике N010";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();
            if (onkSl.getBdiagList() != null && !onkSl.getBdiagList().isEmpty()) {
                for (Bdiag bdiag : onkSl.getBdiagList()) {
                    Integer tip = bdiag.getDiagTip();
                    Integer code = bdiag.getDiagCode();

                    if (tip == null  || code == null) {
                        continue; // npe-safe
                    }

                    if (tip == 2 && !n010Service.isValidCodeOnDate(code, d2)) {
                        errors.add(new FlkErr(zap, sl, null, code));
                    }
                }
            }

            return errors;
        });
    }
}
