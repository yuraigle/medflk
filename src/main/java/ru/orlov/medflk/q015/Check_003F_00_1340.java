package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
public class Check_003F_00_1340 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "В назначениях дата NAPR_DATE должна отсутствовать при виде назначения не 2 и 3";
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

                if ((!List.of(2, 3).contains(nazR) || dsOnk == 0) && naprDate != null) {
                    return List.of(new FlkErr(zap, sl, null, naprDate));
                }
            }

            return List.of();
        });
    }
}
