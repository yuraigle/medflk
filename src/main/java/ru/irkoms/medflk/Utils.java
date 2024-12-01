package ru.irkoms.medflk;

import ru.irkoms.medflk.jaxb.meta.AZlList;

import static org.apache.commons.lang3.StringUtils.right;

public class Utils {

    public static String getZlListMdType(AZlList zlList) {
        return right(zlList.getClass().getSimpleName(), 1); // CHTX
    }

    public static boolean dsIsOnkoC00ToD10OrD45ToD48(String ds1) {
        if (ds1 == null) return false;

        ds1 = ds1.toUpperCase();

        boolean ds1Usl1 = ds1.compareTo("C00.0") >= 0 && ds1.compareTo("D10") < 0;
        boolean ds1Usl2 = ds1.compareTo("D45") >= 0 && ds1.compareTo("D48") < 0;
        return ds1Usl1 || ds1Usl2;
    }

}
