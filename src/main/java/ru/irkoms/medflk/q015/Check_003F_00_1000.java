package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.ASl;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
public class Check_003F_00_1000 {

    public List<FlkP.Pr> check(AZap zap, APers pers) {
        Assert.notNull(pers, "PERS must not be null");

        LocalDate dr = pers.getDr();
        LocalDate d1 = zap.getZSl().getDateZ1();
        if (dr == null || d1 == null) return List.of();
        int age = Period.between(dr, d1).getYears();

        for (ASl sl : zap.getZSl().getSlList()) {
            if (sl.getOnkSl() == null) continue;

            Integer ds1T = sl.getOnkSl().getDs1T();
            Integer onkN = sl.getOnkSl().getOnkN();

            // DS1_T=0 и возраст пациента на DATE_Z_1 больше или равен 18 лет
            if (ds1T == 0 && age >= 18 && onkN == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }
        }

        return List.of();
    }
}
