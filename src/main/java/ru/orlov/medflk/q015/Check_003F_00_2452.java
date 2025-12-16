package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_2452 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            String ds1 = sl.getDs1();
            Integer pOtk = zap.getZSl().getPOtk();

            if (Objects.equals(0, pOtk) && isBlank(ds1)) {
                return List.of(new FlkErr(zap, sl, null, ds1));
            }

            return List.of();
        });
    }
}
