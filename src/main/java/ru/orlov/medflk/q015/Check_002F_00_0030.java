package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
public class Check_002F_00_0030 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Версия файла L должна быть 3.1 до октября 2019";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        Integer yr = zlList.getSchet().getYear();
        Integer mo = zlList.getSchet().getMonth();

        if (yr == null || mo == null) {
            return List.of(); // npe-safe
        }

        LocalDate dFile = LocalDate.of(yr, mo, 1);
        LocalDate dMax = LocalDate.of(2019, 10, 1);
        if (dFile.isAfter(dMax)) {
            return List.of();
        }

        String version = persList.getZglv().getVersion();
        if (!"3.1".equals(version)) {
            return List.of(new FlkErr(null, null, null, version));
        }

        return List.of();
    }
}
