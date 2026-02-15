package ru.orlov.medflk;

import ru.orlov.medflk.jaxb.OnkSl;
import ru.orlov.medflk.jaxb.OnkUsl;

import java.util.Arrays;

public class OmsUtils {

    /**
     * Проверка, находится ли диагноз в диапазоне онко-диагнозов
     *
     * @param ds1 дагноз
     * @return true, если является онко-диагнозом
     */
    public static boolean dsIsOnkoC00ToD10OrD45ToD48(String ds1) {
        if (ds1 == null) return false;

        ds1 = ds1.toUpperCase();

        boolean ds1Usl1 = ds1.compareTo("C00.0") >= 0 && ds1.compareTo("D10") < 0;
        boolean ds1Usl2 = ds1.compareTo("D45") >= 0 && ds1.compareTo("D48") < 0;
        return ds1Usl1 || ds1Usl2;
    }

    /**
     * Проверка, находится ли онко-диагноз в списке диагнозов, по которым
     * допустимо указание детского профиля до 21 года (а не до 18)
     *
     * @param ds1 диагноз
     * @return true, если детский профиль по диагнозу допустим до 21 года
     */
    public static boolean dsIsOnkoDetTill21(String ds1) {
        String[] ds1List = """
                        C40, C49, C62, C64, C70, C71, C72, C81, C95, C22.2, C38.1, C47.3, C47.4,
                        C47.5, C47.6, C47.8, C47.9, C48.0, C74.1, C74.9, C76.0, C76.1, C76.2, C76.3,
                        C76.7, C76.8, C83.3, C83.5, C83.7, C84.6, C84.7, C85.2, C91.0, C91.8, C92.0,
                        C92.3, C92.4, C92.5, C92.6, C92.7, C92.8, C92.9, C93.0, C94.0, C94.2
                """.trim().split("[, \n]+");

        return ds1 != null && Arrays.stream(ds1List).anyMatch(ds1::startsWith);
    }

    /**
     * Проверка, есть ли приём лекарственного препарата в онко-случае
     *
     * @param onkSl  онко-случай
     * @param regnum идентификатор лекарственного средства
     * @return true, если лекарственное средство применялось
     */
    public static boolean onkSlHasRegNum(OnkSl onkSl, String regnum) {
        if (onkSl == null || onkSl.getOnkUslList() == null) return false;

        for (OnkUsl onkUsl : onkSl.getOnkUslList()) {
            if (onkUsl.getLekPrList() == null) continue;

            boolean hasRegNum = onkUsl.getLekPrList().stream()
                    .anyMatch(lp -> lp.getRegnum().equals(regnum));

            if (hasRegNum) {
                return true;
            }
        }

        return false;
    }

}
