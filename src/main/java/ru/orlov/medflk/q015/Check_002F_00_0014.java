package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
public class Check_002F_00_0014 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Версия файла должна быть 5.0";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        Integer yr = zlList.getSchet().getYear();
        Integer mo = zlList.getSchet().getMonth();

        if (yr == null || mo == null) {
            return List.of(); // npe-safe
        }

        LocalDate dFile = LocalDate.of(yr, mo, 1);
        LocalDate dMax = LocalDate.of(2025, 1, 1);
        if (dFile.isBefore(dMax)) {
            return List.of(); // С отчетного периода "январь 2025".
        }

        String version = zlList.getZglv().getVersion();
        if (!"5.0".equals(version)) {
            return List.of(new FlkErr(null, null, null, version));
        }

        return List.of();
    }
}
