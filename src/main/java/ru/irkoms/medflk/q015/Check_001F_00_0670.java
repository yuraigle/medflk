package ru.irkoms.medflk.q015;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irkoms.medflk.domain.F006Service;
import ru.irkoms.medflk.domain.V021Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.Sank;
import ru.irkoms.medflk.jaxb.ZlList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Check_001F_00_0670 extends AbstractCheck {

    private final F006Service f006Service;

    @Override
    public String getErrorMessage() {
        return "Код вида контроля в санкции не найден в справочнике F006";
    }

    @Override
    public List<FlkP.Pr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            // Вообще, в счёте от МО не должно быть санкций
            if (zap.getZSl().getSankList() == null) {
                return List.of();
            }

            List<FlkP.Pr> errors = new ArrayList<>();
            for (Sank sank : zap.getZSl().getSankList()) {
                LocalDate d1 = sank.getDateAct();
                Integer sTip = sank.getSTip();
                if (d1 != null && sTip != null && !f006Service.isValidSTipOnDate(sTip, d1)) {
                    errors.add(new FlkP.Pr(zap, null, sTip));
                }
            }

            return errors;
        });
    }
}
