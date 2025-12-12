package ru.irkoms.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_2870 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, check1());
    }

    IFunctionOverUsl check1() {
        return (zlList, zap, sl, usl) -> {
            if (usl.getProfil() == null) {
                return List.of(new FlkP.Pr(zap, sl, usl, null));
            }

            return List.of();
        };
    }
}
