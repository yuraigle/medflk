package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_0550 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Тип оплаты указан при отсутствии санкций к МО";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            boolean hasSankIst1 = zap.getZSl().getSankList() != null &&
                    zap.getZSl().getSankList().stream()
                            .anyMatch(s -> Objects.equals(1, s.getSIst()));

            if (!hasSankIst1 && zap.getZSl().getOplata() != null && zap.getZSl().getOplata() > 0) {
                return List.of(new FlkP.Pr(zap, null, zap.getZSl().getOplata()));
            }

            return List.of();
        });
    }

}
