package ru.orlov.medflk.q015;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.N001Service;
import ru.orlov.medflk.jaxb.Bprot;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0500 extends AbstractCheck {

    private final N001Service n001Service;

    @Override
    public String getErrorMessage() {
        return "Код противопоказания PROT должен быть найден в справочнике N001";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverOnkSl(zlList, persList, (a, zap, sl, onkSl) -> {
            @NonNull LocalDate d2 = zap.getZSl().getDateZ2();

            List<FlkErr> errors = new ArrayList<>();

            if (onkSl.getBprotList() != null) {
                for (Bprot bProt : sl.getOnkSl().getBprotList()) {
                    Integer prot = bProt.getProt();
                    if (prot != null && !n001Service.isValidProtOnDate(prot, d2)) {
                        errors.add(new FlkErr(zap, sl, null, prot));
                    }
                }
            }

            return errors;
        });
    }
}
