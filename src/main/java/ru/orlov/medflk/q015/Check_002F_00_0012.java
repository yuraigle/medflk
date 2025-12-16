package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.right;

@Component
public class Check_002F_00_0012 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Версия файла T,X должна быть 3.1";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        String version = zlList.getZglv().getVersion();
        String typeMd = right(zlList.getClass().getSimpleName(), 1);

        if (List.of("T", "X").contains(typeMd) && !version.startsWith("3.1")) {
            return List.of(new FlkP.Pr(null, null, version));
        }

        return List.of();
    }
}
