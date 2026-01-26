package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_2080 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Выставленная сумма по счёту должна быть заполнена";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {

        if (zlList.getSchet().getSummav() == null) {
            return List.of(new FlkErr(null, null, null, null));
        }

        return List.of();
    }

}
