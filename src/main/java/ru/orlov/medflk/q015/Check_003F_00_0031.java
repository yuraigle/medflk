package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.*;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Check_003F_00_0031 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Сумма санкций SANK_MEK должна быть заполнена при наличии санкций МЭК";
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

                    boolean isMek = sTip == 1;
                    if (isMek && sSum.compareTo(BigDecimal.ZERO) != 0 && List.of(1, 10, 11, 12).contains(sIst)) {
                        hasSank = true;
                        break;
                    }
                }
            }
        }

        if (hasSank && zlList.getSchet().getSankMek() == null) {
            return List.of(new FlkErr(null, null, null, null));
        }

        return List.of();
    }
}
