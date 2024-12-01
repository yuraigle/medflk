package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.right;

@Component
public class Check_002F_00_0012 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        String version = zlList.getZglv().getVersion();
        String typeMd = right(zlList.getClass().getSimpleName(), 1);

        if (List.of("T", "X").contains(typeMd) && !version.startsWith("3.1")) {
            return List.of(new FlkP.Pr(null, null, version));
        }

        return List.of();
    }
}
