package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_0090 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Номер полиса должен быть заполнен для полисов отличных от ПЕО";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Integer vPolis = zap.getPacient().getVpolis();
            String nPolis = zap.getPacient().getNpolis();

            if (!Objects.equals(3, vPolis) && isBlank(nPolis)) {
                return List.of(new FlkErr(zap, null, null, null));
            }

            return List.of();
        });
    }
}
