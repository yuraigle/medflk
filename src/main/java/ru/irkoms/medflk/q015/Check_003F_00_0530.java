package ru.irkoms.medflk.q015;

import org.springframework.stereotype.Component;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_0530 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Тип оплаты не указан при наличии санкций к МО";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            boolean hasSankIst1 = zap.getZSl().getSankList() != null &&
                    zap.getZSl().getSankList().stream()
                            .anyMatch(s -> Objects.equals(1, s.getSIst()));

            if (hasSankIst1 && zap.getZSl().getOplata() == null) {
                return List.of(new FlkP.Pr(zap, null, null));
            }

            return List.of();
        });
    }

}
