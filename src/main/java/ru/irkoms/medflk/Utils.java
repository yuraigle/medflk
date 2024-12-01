package ru.irkoms.medflk;

import lombok.extern.log4j.Log4j2;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.right;

@Log4j2
public class Utils {

    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> rawCollection) {
        List<T> result = new ArrayList<>(rawCollection.size());
        for (Object o : rawCollection) {
            try {
                result.add(clazz.cast(o));
            } catch (ClassCastException e) {
                log.error("Error while casting: {}", e.getMessage());
            }
        }
        return result;
    }

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
