package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0020 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сумма принятая к оплате SUMMAP обязательна в файле от СМО или ТФОМС";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        String filename = zlList.getZglv().getFilename();
        if (filename == null) {
            return List.of(); // npe-safe
        }

        String i = filename.toUpperCase().replaceAll("^[A-Z]+([A-Z])[0-9]+.*$", "$1");

        if (List.of("S", "T").contains(i) && zlList.getSchet().getSummap() == null) {
            return List.of(new FlkErr(null, null, null, null));
        }

        return List.of();
    }
}
