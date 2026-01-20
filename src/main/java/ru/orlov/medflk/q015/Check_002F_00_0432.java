package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.orlov.medflk.Utils.dsIsOnkoDetTill21;
import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_002F_00_0432 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код схемы CODE_SH должен быть 'нет' при оказании помощи детям";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkUsl(zlList, persList, (a, zap, sl, onkSl, onkUsl) -> {
            @NonNull Pers pers = getPersById(zap.getPacient().getIdPac());

            List<FlkErr> errors = new ArrayList<>();

            if (onkUsl.getLekPrList() != null) {
                for (LekPrOnk lekPr : onkUsl.getLekPrList()) {
                    boolean shouldBeNet = codeShShouldBeNet(pers, zap, sl, onkUsl);

                    String codeSh = lekPr.getCodeSh();
                    if (shouldBeNet && !"нет".equals(codeSh)) {
                        errors.add(new FlkErr(zap, sl, null, codeSh));
                    }
                }
            }

            return errors;
        });
    }

    /*
     (Возраст ЗЛ на дату начала законченного случая (DATE_Z_1)<18 лет)
     ИЛИ (C81.0<=DS1<C97 и USL_TIP>4)
     ИЛИ (D45<=DS1<D48 и USL_TIP>4)
     ИЛИ (Возраст ЗЛ на дату начала законченного случая (DATE_Z_1)<21 года и DET=1 и DS1={...})
     */
    private boolean codeShShouldBeNet(Pers pers, Zap zap, Sl sl, OnkUsl onkUsl) {
        LocalDate d1 = zap.getZSl().getDateZ1();
        LocalDate dr = pers.getDr();
        Integer uslTip = onkUsl.getUslTip();
        String ds1 = sl.getDs1();
        if (dr == null || d1 == null || uslTip == null || ds1 == null) return false; // npe-safe

        int age = Period.between(dr, d1).getYears();
        if (age < 18) {
            return true; // Возраст<18 лет - схема должна быть "нет"
        }

        if (uslTip > 4 && ds1.compareTo("C81.0") >= 0 && ds1.compareTo("C97") < 0) {
            return true; // ИЛИ (C81.0<=DS1<C97 и USL_TIP>4)
        }

        if (uslTip > 4 && ds1.compareTo("D45") >= 0 && ds1.compareTo("D48") < 0) {
            return true; // ИЛИ (D45<=DS1<D48 и USL_TIP>4)
        }

        // ИЛИ Возраст<21 года и DET=1 и DS1={...}
        return age < 21 && Objects.equals(1, sl.getDet()) && dsIsOnkoDetTill21(ds1);
    }
}
