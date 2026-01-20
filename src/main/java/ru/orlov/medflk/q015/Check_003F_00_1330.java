package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
public class Check_003F_00_1330 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Дата NAPR_DATE должна быть указана при направлении на консультацию";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (Naz naz : sl.getNazList()) {
                LocalDate naprDate = naz.getNaprDate();
                Integer nazR = naz.getNazR();
                Integer dsOnk = sl.getDsOnk();

                if (dsOnk == null || nazR == null) continue;

                if (List.of(2, 3).contains(nazR) && dsOnk == 1 && naprDate == null) {
                    return List.of(new FlkErr(zap, sl, null, null));
                }
            }

            return List.of();
        });
    }
}
