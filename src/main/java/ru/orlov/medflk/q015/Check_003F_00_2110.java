package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_2110 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Номер записи N_ZAP должен быть заполнен и быть уникальным";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        long cntZap = zlList.getZapList().size();
        long cntZapUnique = zlList.getZapList().stream().distinct().count();
        if (cntZap != cntZapUnique) {
            return List.of(new FlkErr(null, null, null, null));
        }

        return List.of();
    }
}
