package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1980 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Кол-во записей SD_Z в заголовке должно быть заполнено";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {

        if (zlList.getZglv().getSdZ() == null) {
            return List.of(new FlkErr(null, null, null, null));
        }

        return List.of();
    }

}
