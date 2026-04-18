package ru.orlov.medflk.calc.hospital.app9;

import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.ZSl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExceptionalPlt {


    private final List<String> t1DiagList = List.of("""
            S02.0, S02.1, S04.0, S05.7, S06.1, S06.2, S06.3, S06.4, S06.5,
            S06.6, S06.7, S07.0, S07.1, S07.8, S09.0, S11.0, S11.1, S11.2,
            S11.7, S15.0, S15.1, S15.2, S15.3, S15.7, S15.8, S15.9, S17.0,
            S17.8, S18
            """.split("[, \n]+"));

    private final List<String> t2DiagList = List.of("""
            S12.0, S12.9, S13.0, S13.1, S13.3, S14.0, S14.3, S22.0, S23.0,
            S23.1, S24.0, S32.0, S32.1, S33.0, S33.1, S33.2, S33.4, S34.0,
            S34.3, S34.4
            """.split("[, \n]+"));

    private final List<String> t3DiagList = List.of("""
            S22.2, S22.4, S22.5, S25.0, S25.1, S25.2, S25.3, S25.4, S25.5,
            S25.7, S25.8, S25.9, S26.0, S27.0, S27.1, S27.2, S27.4, S27.5,
            S27.6, S27.8, S28.0, S28.1
            """.split("[, \n]+"));

    private final List<String> t4DiagList = List.of("""
            S35.0, S35.1, S35.2, S35.3, S35.4, S35.5, S35.7, S35.8, S35.9,
            S36.0, S36.1, S36.2, S36.3, S36.4, S36.5, S36.8, S36.9, S37.0,
            S38.3
            """.split("[, \n]+"));

    private final List<String> t5DiagList = List.of("""
            S32.3, S32.4, S32.5, S36.6, S37.1, S37.2, S37.4, S37.5, S37.6,
            S37.8, S38.0, S38.2
            """.split("[, \n]+"));

    private final List<String> t6DiagList = List.of("""
            S42.2, S42.3, S42.4, S42.8, S45.0, S45.1, S45.2, S45.7, S45.8,
            S47 , S48.0, S48.1, S48.9, S52.7, S55.0, S55.1, S55.7, S55.8,
            S57.0, S57.8, S57.9, S58.0, S58.1, S58.9, S68.4, S71.7, S72.0,
            S72.1, S72.2, S72.3, S72.4, S72.7, S75.0, S75.1, S75.2, S75.7,
            S75.8, S77.0, S77.1, S77.2, S78.0, S78.1, S78.9, S79.7, S82.1,
            S82.2, S82.3, S82.7, S85.0, S85.1, S85.5, S85.7, S87.0, S87.8,
            S88.0, S88.1, S88.9, S95.7, S95.8, S95.9, S97.0, S97.8, S98.0
            """.split("[, \n]+"));

    private final List<String> t7DiagList = List.of("""
            S02.7, S12.7, S22.1, S27.7, S29.7, S31.7, S32.7, S36.7, S38.1,
            S39.6, S39.7, S37.7, S42.7, S49.7, T01.1, T01.8, T01.9, T02.0,
            T02.1, T02.2, T02.3, T02.4, T02.5, T02.6, T02.7, T02.8, T02.9,
            T04.0, T04.1, T04.2, T04.3, T04.4, T04.7, T04.8, T04.9, T05.0,
            T05.1, T05.2, T05.3, T05.4, T05.5, T05.6, T05.8, T05.9, T06.0,
            T06.1, T06.2, T06.3, T06.4, T06.5, T06.8, T07
            """.split("[, \n]+"));

    private final List<String> dopDiagList = List.of("""
            J94.2, J94.8, J94.9, J93, J93.0, J93.1, J93.8, J93.9, J96.0,
            N17, T79.4, R57.1, R57.8
            """.split("[, \n]+"));

    public boolean isExceptional(ZSl zSl, String slId, String ksg) {
        Sl sl = zSl.getSlList().stream()
                .filter(sl1 -> sl1.getSlId().equals(slId)).findFirst().orElseThrow();

        List<String> critList = sl.getKsgKpg() != null && sl.getKsgKpg().getCritList() != null ?
                sl.getKsgKpg().getCritList() : List.of();
        boolean hasPlt = critList.stream().anyMatch(s -> s.equals("plt"));

        if (!hasPlt) {
            return false;
        }

        String ds1 = sl.getDs1();
        Set<String> ds2Set = sl.getDs2List() == null ? new HashSet<>() :
                new HashSet<>(sl.getDs2List());
        Set<String> ds3Set = sl.getDs3List() == null ? new HashSet<>() :
                new HashSet<>(sl.getDs3List());

        Set<String> allDs = new HashSet<>(Collections.singleton(ds1));
        allDs.addAll(ds2Set);
        allDs.addAll(ds3Set);

        Set<String> anat = new HashSet<>();
        if (allDs.stream().anyMatch(t1DiagList::contains)) {
            anat.add("T1");
        }
        if (allDs.stream().anyMatch(t2DiagList::contains)) {
            anat.add("T2");
        }
        if (allDs.stream().anyMatch(t3DiagList::contains)) {
            anat.add("T3");
        }
        if (allDs.stream().anyMatch(t4DiagList::contains)) {
            anat.add("T4");
        }
        if (allDs.stream().anyMatch(t5DiagList::contains)) {
            anat.add("T5");
        }
        if (allDs.stream().anyMatch(t6DiagList::contains)) {
            anat.add("T6");
        }
        if (allDs.stream().anyMatch(t7DiagList::contains)) {
            anat.add("T7");
        }

        boolean hasDopDs = ds2Set.stream().anyMatch(dopDiagList::contains)
                || ds3Set.stream().anyMatch(dopDiagList::contains);

        if ("st29.007".equals(ksg) && (anat.contains("T7") || anat.size() >= 2) && hasDopDs) {
            return true;
        }

        return false;
    }

}
