package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;

import static ru.irkoms.medflk.Utils.getZlListMdType;

@Component
public class Check_003F_00_1710 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        String typeMd = getZlListMdType(zlList);
        if (!List.of("H", "T", "C").contains(typeMd)) return List.of();

        for (PersList.Pers pers : persList.getPersList()) {
            if (pers.getTel() != null) {
                return List.of(new FlkP.Pr(pers, pers.getTel()));
            }
        }

        return List.of();
    }
}
