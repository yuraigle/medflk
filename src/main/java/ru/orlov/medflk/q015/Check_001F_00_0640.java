package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V019Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0640 extends AbstractCheck {

    private final V019Service v019Service;

    @Override
    public String getErrorMessage() {
        return "Вид медицинского вмешательства VID_VME не найден в справочнике V019";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverUsl(zlList, persList, (a, zap, sl, usl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            String vme = usl.getVidVme();

            if (vme == null) {
                return List.of(); // npe-safe
            }

            // в реестре ВТ VID_VME должен быть числом
            int met;
            try {
                met = Integer.parseInt(vme);
            } catch (NumberFormatException e) {
                return List.of(new FlkErr(zap, sl, usl, vme));
            }

            if (!v019Service.isValidMetOnDate(met, d2)) {
                return List.of(new FlkErr(zap, sl, usl, met));
            }

            return List.of();
        });
    }
}
