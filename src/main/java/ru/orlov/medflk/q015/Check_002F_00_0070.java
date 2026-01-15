package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0070 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Месяц счёта должен быть от 1 до 12";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        Integer month = zlList.getSchet().getMonth();

        if (month == null) {
            return List.of(); // npe-safe
        }

        if (month < 1 || month > 12) {
            return List.of(new FlkErr(null, null, null, month));
        }

        return List.of();
    }
}
