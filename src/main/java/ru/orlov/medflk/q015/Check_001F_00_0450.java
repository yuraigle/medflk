package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.M002Service;
import ru.orlov.medflk.domain.nsi.N005Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0450 extends AbstractCheck {

    private final N005Service n005Service;
    private final M002Service m002Service;

    @Override
    public String getErrorMessage() {
        return "Значение Metastasis не находится в справочнике N005 для диагноза";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();
            String ds1 = sl.getDs1();
            Integer onkM = onkSl.getOnkM();

            if (ds1 == null || onkM == null) {
                return List.of(); // npe-safe
            }

            // подменяем DS согласно справочнику соответствия МКБ-10 и МКБ-О
            String dsO = m002Service.findMkbOTopography(ds1);

            if (dsO != null && !n005Service.isValidMetastasisOnDsAndDate(onkM, dsO, d2)) {
                return List.of(new FlkErr(zap, sl, null, onkM));
            }

            return List.of();
        });
    }
}
