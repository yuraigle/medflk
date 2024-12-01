package ru.irkoms.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ANaz;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_1380 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (ANaz naz : sl.getNazList()) {
                Integer nazPmp = naz.getNazPmp();
                Integer nazR = naz.getNazR();

                if (nazR != null && !List.of(4, 5).contains(nazR) && nazPmp != null) {
                    return List.of(new FlkP.Pr(zap, sl, nazPmp));
                }
            }

            return List.of();
        });
    }
}
