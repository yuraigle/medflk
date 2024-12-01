package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ANaz;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class Check_003F_00_1340 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, check1());
    }

    private IFunctionOverSl check1() {
        return (zlList, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (ANaz naz : sl.getNazList()) {
                LocalDate naprDate = naz.getNaprDate();
                Integer nazR = naz.getNazR();
                Integer dsOnk = sl.getDsOnk();

                if (dsOnk == null || nazR == null) continue;

                if ((!List.of(2, 3).contains(nazR) || dsOnk == 0) && naprDate != null) {
                    return List.of(new FlkP.Pr(zap, sl, naprDate));
                }
            }

            return List.of();
        };
    }
}
