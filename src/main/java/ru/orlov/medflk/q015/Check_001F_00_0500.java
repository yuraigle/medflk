package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Bprot;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.ArrayList;
import java.util.List;

@Component
public class Check_001F_00_0500 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Код противопоказания PROT не из справочника N001";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            List<FlkP.Pr> errors = new ArrayList<>();
            if (onkSl.getBprotList() != null) {
                for (Bprot bProt : sl.getOnkSl().getBprotList()) {
                    Integer prot = bProt.getProt();
                    if (prot != null && !List.of(0, 1, 2, 3, 4, 5, 6, 7, 8).contains(prot)) { // TODO N001
                        errors.add(new FlkP.Pr(zap, sl, prot));
                    }
                }
            }
            return errors;
        });
    }
}
