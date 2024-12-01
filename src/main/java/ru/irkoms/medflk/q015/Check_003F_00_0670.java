package ru.irkoms.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@RequiredArgsConstructor
public class Check_003F_00_0670 extends AbstractCheck {

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, check1());
    }

    IFunctionOverSl check1() {
        return (zlList, zap, sl) -> {
            String pCel = sl.getPCel();
            Integer uslOk = zap.getZSl().getUslOk();

            if (!Objects.equals(3, uslOk) && isNotBlank(pCel)) {
                return List.of(new FlkP.Pr(zap, sl, null));
            }

            return List.of();
        };
    }
}
