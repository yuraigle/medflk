package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1290 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (Naz naz : sl.getNazList()) {
                Integer nazV = naz.getNazV();
                Integer nazR = naz.getNazR();

                if (nazR != null && nazR == 3 && nazV == null) {
                    return List.of(new FlkErr(zap, sl, null, null));
                }
            }

            return List.of();
        });
    }
}
