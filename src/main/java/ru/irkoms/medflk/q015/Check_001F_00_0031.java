package ru.irkoms.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.F032Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0031 extends AbstractCheck {

    private final F032Service f032Service;

    @Override
    public String getErrorMessage() {
        return "Код МО не найден в справочнике F032";
    }

    @Override
    public List<FlkP.Pr> check(AZlList zlList, APersList persList) {
        String codeMo = zlList.getSchet().getCodeMo();
        LocalDate d1 = zlList.getSchet().getDschet();

        if (codeMo == null || d1 == null) return List.of(); // npe-safe

        if (!f032Service.isValidRegionalCodeMoOnDate(codeMo, d1)) {
            return List.of(new FlkP.Pr(null, null, codeMo));
        }

        return List.of();
    }
}
