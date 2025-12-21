package ru.orlov.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V016Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0050 extends AbstractCheck {

    private final V016Service v016Service;

    @Override
    public String getErrorMessage() {
        return "Тип диспансеризации DISP должен быть найден в справочнике V016";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        LocalDate d1 = zlList.getSchet().getDschet();
        String dispType = zlList.getSchet().getDisp();

        if (dispType == null || d1 == null) return List.of(); // npe-safe

        if (!v016Service.isValidDispTypeOnDate(dispType, d1)) {
            return List.of(new FlkErr(null, null, null, dispType));
        }

        return List.of();
    }
}
