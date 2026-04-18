package ru.orlov.medflk.calc.hospital.app9;

import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.ZSl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExceptionalIrs {

    public boolean isExceptional(ZSl zSl, String slId, String ksg) {
        Sl sl = zSl.getSlList().stream()
                .filter(sl1 -> sl1.getSlId().equals(slId)).findFirst().orElseThrow();

        String ds1 = sl.getDs1();
        Set<String> ds2Set = sl.getDs2List() == null ? new HashSet<>() :
                new HashSet<>(sl.getDs2List());
        List<String> critList = sl.getKsgKpg() != null && sl.getKsgKpg().getCritList() != null ?
                sl.getKsgKpg().getCritList() : List.of();

        boolean dsUsl = ds1.equals("Z25.8") || ds2Set.contains("Z25.8");

        if (dsUsl && critList.contains("irs1") && List.of("st36.025", "ds36.012").contains(ksg)) {
            return true; // 0-2мес
        }

        if (dsUsl && critList.contains("irs2") && List.of("st36.026", "ds36.013").contains(ksg)) {
            return true; // 2мес-2г
        }

        return false;
    }

}
