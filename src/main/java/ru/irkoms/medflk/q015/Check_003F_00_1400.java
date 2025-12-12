package ru.irkoms.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Check_003F_00_1400 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (ANaz naz : sl.getNazList()) {
                Integer nazPk = naz.getNazPk();
                Integer nazR = naz.getNazR();

                if (!Objects.equals(6, nazR) && nazPk != null) {
                    return List.of(new FlkP.Pr(zap, sl, nazPk));
                }
            }

            return List.of();
        });
    }
}
