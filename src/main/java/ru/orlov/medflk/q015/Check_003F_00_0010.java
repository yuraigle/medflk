package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0010 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код плательщика должен быть заполнен, кроме межтерриториальных реестров";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        String filename = zlList.getZglv().getFilename();
        if (filename == null) {
            return List.of(); // npe-safe
        }

        String i = filename.toUpperCase().replaceAll("^[A-Z]+([A-Z])[0-9]+.*$", "$1");
        String p = filename.toUpperCase().replaceAll("^[A-Z]+[0-9]+([A-Z])[0-9]+_.*$", "$1");
        String plat = zlList.getSchet().getPlat();

        if (List.of("MS", "ST", "SM").contains(i + p) && isBlank(plat)) {
            return List.of(new FlkErr(null, null, null, plat));
        }

        return List.of();
    }
}
