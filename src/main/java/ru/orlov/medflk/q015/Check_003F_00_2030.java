package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_2030 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код МО в счёте должен быть заполнен";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {

        if (isBlank(zlList.getSchet().getCodeMo())) {
            return List.of(new FlkErr(null, null, null, null));
        }

        return List.of();
    }

}
