package ru.orlov.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Check_003F_00_1390 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (Naz naz : sl.getNazList()) {
                Integer nazPk = naz.getNazPk();
                Integer nazR = naz.getNazR();

                if (Objects.equals(6, nazR) && nazPk == null) {
                    return List.of(new FlkErr(zap, sl, null, null));
                }
            }

            return List.of();
        });
    }
}
