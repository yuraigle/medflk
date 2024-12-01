package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.ABprot;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Check_001F_00_0500 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            if (onkSl.getBprotList() == null) return List.of();

            List<FlkP.Pr> errors = new ArrayList<>();

            for (ABprot bProt : sl.getOnkSl().getBprotList()) {
                LocalDate d1 = zap.getZSl().getDateZ1();
                Integer prot = bProt.getProt();
                if (prot == null || d1 == null) continue; // npe safe

                if (!List.of(0, 1, 2, 3, 4, 5, 6, 7, 8).contains(prot)) { // TODO N001
                    errors.add(new FlkP.Pr(zap, sl, prot));
                }
            }

            return errors;
        });
    }
}
