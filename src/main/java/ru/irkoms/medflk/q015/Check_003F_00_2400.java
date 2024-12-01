package ru.irkoms.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_2400 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, check1());
    }

    IFunctionOverSl check1() {
        return (zlList, zap, sl) -> {
            if (sl.getProfil() == null) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        };
    }
}
