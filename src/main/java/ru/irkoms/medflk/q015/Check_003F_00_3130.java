package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.ASl;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.math.BigDecimal;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Component
public class Check_003F_00_3130 extends AbstractCheckZapWithPers {

    // WEI Обязательно для заполнения с 01.01.2022, если в DS1 указано значение заболевания
    // (U07.1 или U07.2) и REAB <> 1 и CRIT <> STT5 и USL_OK = 1 и DS2 <> IN (O00-O99, Z34-Z35)
    // и возраст пациента на дату начала лечения больше или равно 18 лет

    @Override
    public List<FlkP.Pr> check(AZap zap, APers pers) {
        List<FlkP.Pr> errors = new ArrayList<>();

        Integer uslOk = zap.getZSl().getUslOk();

        for (ASl sl : zap.getZSl().getSlList()) {
            String ds1 = sl.getDs1();
            int reab = sl.getReab() == null ? 0 : sl.getReab();
            if (!List.of("U07.1", "U07.2").contains(ds1)) continue;
            if (reab == 1 || uslOk != 1) continue;

            boolean hasCritStt5 = false;
            if (sl.getKsgKpg() != null && sl.getKsgKpg().getCritList() != null) {
                hasCritStt5 = sl.getKsgKpg().getCritList()
                        .stream().anyMatch(c -> c.equalsIgnoreCase("STT5"));
            }
            if (hasCritStt5) continue;

            boolean hasDs2InO = false;
            boolean hasDs2InZ = false;
            if (sl.getDs2List() != null) {
                hasDs2InO = sl.getDs2List()
                        .stream().anyMatch(s -> s.toUpperCase().startsWith("O"));
                hasDs2InZ = sl.getDs2List()
                        .stream().anyMatch(s -> s.toUpperCase().matches("^Z3[45].*$"));
            }
            if (hasDs2InO || hasDs2InZ) continue; // если беременность, не проверяем

            int age = Period.between(pers.getDr(), zap.getZSl().getDateZ1()).getYears();
            if (age < 18) continue; // если <18 лет, не проверяем

            BigDecimal wei = sl.getWei(); // если все звезды сошлись, WEI обязателен
            if (wei == null) {
                errors.add(new FlkP.Pr(zap, sl, null));
            }
        }

        return errors;
    }

}
