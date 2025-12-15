package ru.irkoms.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.V023Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0580 extends AbstractCheck {

    private final V023Service v023Service;

    @Override
    public String getErrorMessage() {
        return "Номер КСГ N_KSG не найден в справочнике V023";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            if (sl.getKsgKpg() == null) {
                return List.of();
            }

            String nKsg = sl.getKsgKpg().getNKsg();
            Integer ksgPg = sl.getKsgKpg().getKsgPg();
            if (ksgPg == null || ksgPg == 0) {
                if (nKsg != null && !v023Service.isValidNKsgOnDate(nKsg, d2)) {
                    return List.of(new FlkP.Pr(zap, sl, nKsg));
                }
            }

            return List.of();
        });
    }
}
