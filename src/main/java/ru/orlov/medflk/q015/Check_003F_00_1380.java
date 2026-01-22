package ru.orlov.medflk.q015;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Naz;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_003F_00_1380 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "В назначениях профиль помощи NAZ_PMP должен отсутствовать при виде назначения не 4 или 5";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            if (sl.getNazList() == null) return List.of();

            for (Naz naz : sl.getNazList()) {
                Integer nazPmp = naz.getNazPmp();
                Integer nazR = naz.getNazR();

                if (nazR != null && !List.of(4, 5).contains(nazR) && nazPmp != null) {
                    return List.of(new FlkErr(zap, sl, null, nazPmp));
                }
            }

            return List.of();
        });
    }
}
