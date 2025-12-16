package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_002F_00_0050 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Версия файла L должна быть 3.2";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        String version = persList.getZglv().getVersion();

        if (!version.startsWith("3.2")) {
            return List.of(new FlkP.Pr(null, null, version));
        }

        return List.of();
    }
}
