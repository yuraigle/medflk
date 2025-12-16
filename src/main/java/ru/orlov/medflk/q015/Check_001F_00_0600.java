package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V023Packet;
import ru.orlov.medflk.domain.nsi.V023Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0600 extends AbstractCheck {

    private final V023Service v023Service;

    @Override
    public String getErrorMessage() {
        return "Коэффициент затратоемкости KOEF_Z не соответствует найденному в справочнике V023";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            if (sl.getKsgKpg() == null) {
                return List.of();
            }

            String nKsg = sl.getKsgKpg().getNKsg();
            Integer ksgPg = sl.getKsgKpg().getKsgPg();
            BigDecimal koefZ = sl.getKsgKpg().getKoefZ();
            if (ksgPg == null || ksgPg == 0) {
                if (koefZ != null && nKsg != null) {
                    V023Packet.V023 ksg = v023Service.getKsgOnDate(nKsg, d2);
                    if (ksg != null && !koefZ.equals(ksg.getKoefZ())) {
                        return List.of(new FlkErr(zap, sl, null, koefZ));
                    }
                }
            }

            return List.of();
        });
    }
}
