package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V026Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0590 extends AbstractCheck {

    private final V026Service v026Service;

    @Override
    public String getErrorMessage() {
        return "Номер КПГ N_KPG не найден в справочнике V026";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            if (sl.getKsgKpg() == null) {
                return List.of();
            }

            String nKpg = sl.getKsgKpg().getNKpg();
            if (nKpg != null && !v026Service.isValidNKpgOnDate(nKpg, d2)) {
                return List.of(new FlkP.Pr(zap, sl, nKpg));
            }

            return List.of();
        });
    }
}
