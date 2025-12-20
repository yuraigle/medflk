package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N011Service;
import ru.orlov.medflk.jaxb.Bdiag;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0490 extends AbstractCheck {

    private final N011Service n011Service;

    @Override
    public String getErrorMessage() {
        return "Код результата диагностики DIAG_RSLT должен быть найден в справочнике N011";
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
                    Integer res = bdiag.getDiagRslt();

                    if (tip == null || res == null || code == null) {
                        continue; // npe-safe
                    }

                    if (tip == 2 && !n011Service.isValidResOnCodeAndDate(res, code, d2)) {
                        errors.add(new FlkErr(zap, sl, null, res));
                    }
                }
            }

            return errors;
        });
    }
}
