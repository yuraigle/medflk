package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.Pacient;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.right;

@Component
public class Check_003F_00_0140 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "ОКАТО территории страхования должен быть заполнен, если не указана СМО";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        String typeMd = right(zlList.getClass().getSimpleName(), 1);
        if ("H".equals(typeMd)) return List.of(); // в версии 4.0 поля нет по Q018

        return iterateOverZap(zlList, persList, (a, zap) -> {
            Pacient pac = zap.getPacient();
            if (isBlank(pac.getSmo()) && isBlank(pac.getSmoOk())) {
                return List.of(new FlkP.Pr(zap, null, null));
            }
            return List.of();
        });
    }
}
