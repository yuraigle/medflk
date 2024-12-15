package ru.irkoms.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import static ru.irkoms.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_0990 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Значение Tumor должно быть заполнено при DS1_T=0 и возрасте >18";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            @NonNull APers pers = getPersById(zap.getPacient().getIdPac());
            @NonNull LocalDate dr = pers.getDr();
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            int age = Period.between(dr, d1).getYears();

            Integer ds1T = sl.getOnkSl().getDs1T();
            Integer onkT = sl.getOnkSl().getOnkT();

            // DS1_T=0 и возраст пациента на DATE_Z_1 больше или равен 18 лет
            if (Objects.equals(0, ds1T) && age >= 18 && onkT == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
