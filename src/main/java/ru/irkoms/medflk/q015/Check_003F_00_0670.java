package ru.irkoms.medflk.q015;

import lombok.NonNull;
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
    public String getErrorMessage() {
        return "Цель посещения заполнена для случая не АПУ";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull Integer uslOk = zap.getZSl().getUslOk();
            String pCel = sl.getPCel();

            if (!Objects.equals(3, uslOk) && isNotBlank(pCel)) {
                return List.of(new FlkP.Pr(zap, sl, pCel));
            }

            return List.of();
        });
    }
}
