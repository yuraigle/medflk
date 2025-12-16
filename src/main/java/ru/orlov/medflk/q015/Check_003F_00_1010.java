package ru.orlov.medflk.q015;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static ru.orlov.medflk.service.Q015ValidationService.getPersById;

@Component
public class Check_003F_00_1010 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Значение Metastasis должно быть заполнено при DS1_T=0 и возрасте >18";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            @NonNull PersList.Pers pers = getPersById(zap.getPacient().getIdPac());
            @NonNull LocalDate dr = pers.getDr();
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();

            int age = Period.between(dr, d1).getYears();
            Integer ds1T = sl.getOnkSl().getDs1T();
            Integer onkM = sl.getOnkSl().getOnkM();

            // DS1_T=0 и возраст пациента на DATE_Z_1 больше или равен 18 лет
            if (ds1T == 0 && age >= 18 && onkM == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        });
    }
}
