package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

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
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
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
