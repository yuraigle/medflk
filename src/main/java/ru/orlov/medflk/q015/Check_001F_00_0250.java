package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.V018Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0250 extends AbstractCheck {

    private final V018Service v018Service;

    @Override
    public String getErrorMessage() {
        return "Вид ВМП VID_HMP не найден в справочнике V018";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            @NonNull LocalDate d1 = zap.getZSl().getDateZ1();
            String vid = sl.getVidHmp();

            if (vid != null && !v018Service.isValidHvidOnDate(vid, d1)) {
                return List.of(new FlkErr(zap, sl, null, vid));
            }

            return List.of();
        });
    }
}
