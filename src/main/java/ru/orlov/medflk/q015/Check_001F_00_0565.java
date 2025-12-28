package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N021Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.LekPrOnk;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0565 extends AbstractCheck {

    private final N021Service n021Service;

    @Override
    public String getErrorMessage() {
        return "Расширенный идентификатор REGNUM_DOP должен быть найден в справочнике N021 для схемы";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (onkUsl.getLekPrList() != null) {
                for (LekPrOnk lekPrOnk : onkUsl.getLekPrList()) {
                    String codeSh = lekPrOnk.getCodeSh();
                    String regnumDop = lekPrOnk.getRegnumDop();

                    if (codeSh == null || regnumDop == null) {
                        continue; // npe-safe
                    }

                    if (!n021Service.isValidRegnumDopOnCodeShAndDate(regnumDop, codeSh, d2)) {
                        errors.add(new FlkErr(zap, sl, null, regnumDop));
                    }
                }
            }

            return errors;
        });
    }
}
