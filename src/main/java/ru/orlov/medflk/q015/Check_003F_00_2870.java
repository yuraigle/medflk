package ru.orlov.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_2870 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            if (usl.getProfil() == null) {
                return List.of(new FlkErr(zap, sl, usl, null));
            }

            return List.of();
        });
    }
}
