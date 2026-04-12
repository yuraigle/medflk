package ru.orlov.medflk.calc.hospital.app9;

import org.springframework.stereotype.Service;
import ru.orlov.medflk.calc.hospital.domain.GroupKsg;
import ru.orlov.medflk.calc.hospital.domain.GroupKsgRepo;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.Usl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExceptionalChildBirth {

    private final Set<String> dsChildBirth003;
    private final Set<String> uslChildBirth012;
    private final Set<String> uslChildBirth013;
    private final Set<String> uslNewBorn003;

    public ExceptionalChildBirth(GroupKsgRepo groupKsgService) {
        uslNewBorn003 = Set.of("B01.001.006", "B01.001.009", "B02.001.002",
                "A16.20.007", "A16.20.015", "A16.20.023", "A16.20.024", "A16.20.030");

        dsChildBirth003 = groupKsgService.getGrouper(1).stream()
                .filter(g -> "st02.003".equals(g.getNKsg()))
                .map(GroupKsg::getDs1)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        uslChildBirth012 = groupKsgService.getGrouper(1).stream()
                .filter(g -> "st02.012".equals(g.getNKsg()))
                .map(GroupKsg::getCodeUsl)
                .filter(Objects::nonNull)
                .filter(s -> !uslNewBorn003.contains(s))
                .collect(Collectors.toSet());

        uslChildBirth013 = groupKsgService.getGrouper(1).stream()
                .filter(g -> "st02.013".equals(g.getNKsg()))
                .map(GroupKsg::getCodeUsl)
                .filter(Objects::nonNull)
                .filter(s -> !uslNewBorn003.contains(s))
                .collect(Collectors.toSet());
    }

    public boolean isExceptional(Sl sl, String ksg) {
        String ds1 = sl.getDs1();
        Set<String> uslSet = new HashSet<>();
        if (sl.getUslList() != null) {
            uslSet = sl.getUslList().stream()
                    .map(Usl::getCodeUsl)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }

        // Отнесение к КСГ st02.003 «Родоразрешение» при любом основном
        // диагнозе класса XV. Беременность, роды и послеродовой период (O00-O99),
        // включенном в данную КСГ, производится при комбинации с любой
        // из следующих услуг
        if (ksg.equals("st02.003") && dsChildBirth003.contains(ds1)
                && uslSet.stream().anyMatch(uslNewBorn003::contains)
        ) {
            return true;
        }

        // Если при наличии диагноза класса (O00-O99) нет закодированных услуг,
        // соответствующих родоразрешению, случай относится к КСГ st02.001 «Осложнения».
        if (ksg.equals("st02.001") && ds1.startsWith("O")
                && uslSet.stream().noneMatch(uslNewBorn003::contains)
        ) {
            return true;
        }

        // При выполнении операции кесарева сечения случай относится к КСГ st02.004 вне зависимости от диагноза
        if (ksg.equals("st02.004") && uslSet.contains("A16.20.005")) {
            return true;
        }

        // Если в ходе оказания медицинской помощи роженице выполнялась операция,
        // входящая в КСГ st02.012 или st02.013, отнесение случая производится к КСГ по коду операции
        if (ksg.equals("st02.012") && uslSet.stream().anyMatch(uslChildBirth012::contains)) {
            return true;
        }

        if (ksg.equals("st02.013") && uslSet.stream().anyMatch(uslChildBirth013::contains)) {
            return true;
        }

        return false;
    }
}
