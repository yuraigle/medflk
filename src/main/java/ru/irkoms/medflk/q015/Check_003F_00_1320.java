package ru.irkoms.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ANaz;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class Check_003F_00_1320 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (ANaz naz : sl.getNazList()) {
                Integer dsOnk = sl.getDsOnk();
                Integer nazR = naz.getNazR();
                String nazUsl = naz.getNazUsl();
                if (dsOnk == null || nazR == null) continue;

                if ((nazR != 3 || dsOnk == 0) && nazUsl != null) {
                    return List.of(new FlkP.Pr(zap, sl, nazUsl));
                }
            }

            return List.of();
        });
    }
}
