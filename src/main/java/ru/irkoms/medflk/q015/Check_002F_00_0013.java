package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.right;

@Component
public class Check_002F_00_0013 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Версия файла C,H должна быть 4.0";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        String version = zlList.getZglv().getVersion();
        String typeMd = right(zlList.getClass().getSimpleName(), 1);

        if (List.of("C", "H").contains(typeMd) && !version.startsWith("4.0")) {
            return List.of(new FlkP.Pr(null, null, version));
        }

        return List.of();
    }
}
