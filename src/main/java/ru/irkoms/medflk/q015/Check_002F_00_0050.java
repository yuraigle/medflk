package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
public class Check_002F_00_0050 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        String version = persList.getZglv().getVersion();

        if (!version.startsWith("3.2")) {
            return List.of(new FlkP.Pr(null, null, version));
        }

        return List.of();
    }
}
