package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_0570 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сумма принятая к оплате SUMP обязательна в файле от СМО или ТФОМС";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        String filename = zlList.getZglv().getFilename();
        if (filename == null) {
            return List.of(); // npe-safe
        }

        String i = filename.toUpperCase().replaceAll("^[A-Z]+([A-Z])[0-9]+.*$", "$1");
        if (!List.of("S", "T").contains(i)) {
            return List.of();
        }

        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getZSl().getSump() == null) {
                return List.of(new FlkErr(zap, null, null, null));
            }
            return List.of();
        });
    }

}
