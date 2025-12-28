package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V024Service;
import ru.orlov.medflk.jaxb.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.orlov.medflk.Utils.dsIsOnkoDetTill21;
import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0573 extends AbstractCheck {

    private final V024Service v024Service;

    @Override
    public String getErrorMessage() {
        return "Код схемы лекарственной терапии CODE_SH должен быть найден в справочнике V024";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (onkUsl.getLekPrList() != null) {
                @NonNull Pers pers = getPersById(zap.getPacient().getIdPac());
                for (LekPrOnk lekPrOnk : onkUsl.getLekPrList()) {
                    String codeSh = lekPrOnk.getCodeSh();

                    boolean shouldBeV024 = codeShShouldBeV024(pers, zap, sl, onkUsl);
                    if (codeSh != null && shouldBeV024 && !v024Service.isValidCritOnDate(codeSh, d2)) {
                        errors.add(new FlkErr(zap, sl, null, codeSh));
                    }
                }
            }

            return errors;
        });
    }

    /*
        (Возраст ЗЛ>=21 года и (C00.0<=DS1<C81 или (C81.0<=DS1<C97 и USL_TIP=2) или C96<DS1<D10
            или (D45<=DS1<D48 и USL_TIP=2)))
        ИЛИ
        (Возраст ЗЛ>=18 лет и (C00.0<=DS1<C81 или (C81.0<=DS1<C97 и USL_TIP=2) или C96<DS1<D10
            или (D45<=DS1<D48 и USL_TIP=2)) и (DS1={...}) и DET=0)
     */
    private boolean codeShShouldBeV024(Pers pers, Zap zap, Sl sl, OnkUsl onkUsl) {
        LocalDate d1 = zap.getZSl().getDateZ1();
        LocalDate dr = pers.getDr();
        String ds1 = sl.getDs1();
        Integer uslTip = onkUsl.getUslTip();
        if (d1 == null || dr == null || ds1 == null || uslTip == null) return false; // npe-safe

        boolean isDsC00ToC81 = ds1.compareTo("C00.0") >= 0 && ds1.compareTo("C81") < 0;
        boolean isDsC81ToC97 = ds1.compareTo("C81.0") >= 0 && ds1.compareTo("C97") < 0;
        boolean isDsC96ToD10 = ds1.compareTo("C96") > 0 && ds1.compareTo("D10") < 0;
        boolean isDsD45ToD48 = ds1.compareTo("D45") >= 0 && ds1.compareTo("D48") < 0;
        int age = Period.between(dr, d1).getYears();
        boolean isAdult = age >= 21 || (age >= 18 && dsIsOnkoDetTill21(ds1) && Objects.equals(0, sl.getDet()));

        if (!isAdult) return false;
        return isDsC00ToC81 || (isDsC81ToC97 && uslTip == 2) || isDsC96ToD10
                || (uslTip == 2 && isDsD45ToD48);
    }
}
