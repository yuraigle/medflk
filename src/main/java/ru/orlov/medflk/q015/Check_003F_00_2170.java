package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Pacient;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_2170 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            Pacient pac = zap.getPacient();

            if (Objects.equals(3, pac.getVpolis()) && !isBlank(pac.getEnp())) {
                return List.of(); // OK
            } else if (isBlank(pac.getNpolis())) {
                return List.of(new FlkErr(zap, null, null, null)); // ошибка: надо NPOLIS
            }

            return List.of();
        });
    }
}
