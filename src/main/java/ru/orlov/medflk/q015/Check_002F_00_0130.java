package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Component
public class Check_002F_00_0130 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Признак новорожденного NOVOR должен быть 0 или ПДДММГГН";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        return iterateOverZap(zlList, persList, (a, zap) -> {
            if (zap.getPacient() == null) return List.of();

            LocalDate d1 = zap.getZSl().getDateZ1();
            LocalDate d2 = zap.getZSl().getDateZ2();
            String novor = zap.getPacient().getNovor();

            if (novor == null || d1 == null || d2 == null) return List.of();

            boolean isValid = false;
            if (novor.equals("0")) {
                isValid = true;
            } else if (novor.matches("^[12]\\d{7,8}$")) {// 125068510
                String dd = novor.substring(1, 3);
                String mm = novor.substring(3, 5);
                String yy = novor.substring(5, 7);

                try {
                    int y = 2000 + Integer.parseInt(yy);
                    int m = Integer.parseInt(mm);
                    int d = Integer.parseInt(dd);
                    LocalDate dr = LocalDate.of(y, m, d);

                    LocalDate dMin = d1.minusYears(1);
                    LocalDate dMax = d2.plusDays(1);
                    isValid = dr.isAfter(dMin) && dr.isBefore(dMax);
                } catch (DateTimeException | NumberFormatException ignore) {
                }
            }

            if (!isValid) {
                return List.of(new FlkErr(zap, null, null, novor));
            }

            return List.of();
        });
    }
}
