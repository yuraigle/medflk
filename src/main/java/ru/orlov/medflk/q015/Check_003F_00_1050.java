package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.OnkSl;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

@Component
public class Check_003F_00_1050 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Кол-во фракций K_FR обязательно для лучевой или химиолучевой терапии";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverSl(zlList, persList, (a, zap, sl) -> {
            OnkSl onkSl = sl.getOnkSl();
            if (onkSl == null) return List.of();

            boolean hasOnkUsl34 = false;
            if (onkSl.getOnkUslList() != null) {
                hasOnkUsl34 = onkSl.getOnkUslList().stream()
                        .anyMatch(u -> u.getUslTip() != null && List.of(3, 4).contains(u.getUslTip()));
            }

            if (hasOnkUsl34 && onkSl.getKFr() == null) {
                return List.of(new FlkErr(zap, sl, null, null));
            }

            return List.of();
        });
    }
}
