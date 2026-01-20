package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.*;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_003F_00_0043 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сумма санкций SANK_MEE должна быть заполнена при наличии санкций МЭЭ";
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

                    boolean isMee = (sTip > 50 && sTip < 59) || sTip == 88 || (sTip > 89 && sTip < 94);
                    if (isMee && sSum.compareTo(BigDecimal.ZERO) != 0 && sIst == 7) {
                        hasSank = true;
                        break;
                    }
                }
            }
        }

        if (hasSank && zlList.getSchet().getSankMee() == null) {
            return List.of(new FlkErr(null, null, null, null));
        }

        return List.of();
    }
}
