package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.*;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_003F_00_0052 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сумма санкций SANK_EKMP должна быть заполнена при наличии санкций ЭКМП";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        boolean hasSank = false;

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl().getSankList() != null) {
                for (Sank sank : zap.getZSl().getSankList()) {
                    Integer sTip = sank.getSTip();
                    BigDecimal sSum = sank.getSSum();
                    Integer sIst = sank.getSIst();

                    if (sTip == null || sSum == null || sIst == null) {
                        continue; // npe-safe
                    }

                    boolean isEkmp = (sTip > 69 && sTip <= 87) || sTip == 89;
                    if (isEkmp && sSum.compareTo(BigDecimal.ZERO) != 0 && sIst == 1) {
                        hasSank = true;
                        break;
                    }
                }
            }
        }

        if (hasSank && zlList.getSchet().getSankEkmp() == null) {
            return List.of(new FlkErr(null, null, null, null));
        }

        return List.of();
    }
}
