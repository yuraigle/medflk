package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.Bdiag;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;
import java.util.Objects;

@Component
public class Check_003F_00_1250 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Результат диагностики DIAG_RSLT должен быть заполнен при признаке получения";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            if (onkSl.getBdiagList() == null) return List.of();

            for (Bdiag bDiag : onkSl.getBdiagList()) {
                Integer diagRslt = bDiag.getDiagRslt();
                Integer recRslt = bDiag.getRecRslt();

                if (Objects.equals(1, recRslt) && diagRslt == null) {
                    return List.of(new FlkErr(zap, sl, null, null));
                }
            }

            return List.of();
        });
    }
}
