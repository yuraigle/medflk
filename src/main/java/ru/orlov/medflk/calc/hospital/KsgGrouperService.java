package ru.orlov.medflk.calc.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.Pers;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.Usl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KsgGrouperService {

    private final KsgGrouperRepo ksgGrouperRepo;

    private static boolean isDsMatch(String slDs, String ksgDsTest) {
        if (ksgDsTest == null) {
            return true; // нет условия по диагнозу
        }

        int size = ksgDsTest.length();
        if (size == 2) { // C. , I. - МР стр. 162
            return slDs.startsWith(ksgDsTest.substring(0, 1));
        } else if (size == 3 || size == 5 || size == 6) { // A00.0, S02.00
            return slDs.equals(ksgDsTest);
        } else if (size == 7) { // C00-C80
            String s1 = ksgDsTest.substring(0, 3);
            String s2 = ksgDsTest.substring(4, 7);
            String sMo = slDs.substring(0, 3);

            // Диапазоны тоже включаются в фильтр
            return sMo.compareTo(s1) >= 0 && sMo.compareTo(s2) <= 0;
        }

        return false;
    }

    private static boolean isAnyDsMatch(List<String> slDs, String ksgDsTest) {
        if (ksgDsTest == null) {
            return true; // нет условия по диагнозу
        }

        // в списке диагнозов должен быть хотя бы один удовлетворяющий условию
        return slDs.stream().anyMatch(ds -> isDsMatch(ds, ksgDsTest));
    }

    private static boolean isCodeUslMatch(List<String> codeUslList, String ksgUslTest) {
        if (ksgUslTest == null) {
            return true; // нет условия по коду услуги
        }

        // в списке оказанных услуг должна быть хотя бы одна с таким кодом
        return codeUslList.stream().anyMatch(code -> code.equals(ksgUslTest));
    }


    private static boolean isDkkMatch(List<String> critList, String ksgDkkTest) {
        if (ksgDkkTest == null) {
            return true; // нет условия по дкк
        }

        return critList.stream().anyMatch(crit -> crit.equals(ksgDkkTest));
    }

    private static boolean isFrakMatch(List<String> critList, String ksgFrakTest) {
        if (ksgFrakTest == null) {
            return true; // нет условия по фракции
        }

        return critList.stream().anyMatch(crit -> crit.equals(ksgFrakTest));
    }

    private static boolean isAgeMatch(LocalDate dr, LocalDate date1, Integer ksgAgeTest) {
        if (ksgAgeTest == null) {
            return true; // нет условия по возрасту
        }

        int days = (int) ChronoUnit.DAYS.between(dr, date1);
        int years = (int) ChronoUnit.YEARS.between(dr, date1);

        // 2.2 Справочник категорий возраста (МР стр. 160)
        if (ksgAgeTest == 1) {
            return days >= 0 && days <= 28;
        } else if (ksgAgeTest == 2) {
            return days >= 29 && days <= 90;
        } else if (ksgAgeTest == 3) {
            return days >= 91 && years < 1;
        } else if (ksgAgeTest == 4) {
            return days >= 0 && years < 2;
        } else if (ksgAgeTest == 5) {
            return days >= 0 && years < 18;
        } else if (ksgAgeTest == 6) {
            return years >= 18;
        } else if (ksgAgeTest == 7) {
            return days >= 0 && years < 21;
        }

        return false;
    }

    private static boolean isKdMatch(Integer kd, Integer ksgKdTest) {
        if (ksgKdTest == null) {
            return true; // нет условия по коду диагноза
        }

        // Длительность пребывания, МР стр. 159
        if (ksgKdTest == 1) {
            return kd <= 3;
        } else if (ksgKdTest == 2) {
            return kd >= 4 && kd <= 10;
        } else if (ksgKdTest == 3) {
            return kd >= 11 && kd <= 20;
        } else if (ksgKdTest == 4) {
            return kd >= 21 && kd <= 30;
        } else if (ksgKdTest == 5) {
            return kd == 30;
        }

        return false;
    }

    public List<GroupKsg> findAllPossibleKsg(Sl sl, Pers pers, Integer uslOk, Integer kd) {
        final String ds1 = sl.getDs1();
        final List<String> ds2List = sl.getDs2List() == null ? List.of() : sl.getDs2List();
        final List<String> ds3List = sl.getDs3List() == null ? List.of() : sl.getDs3List();
        final int sex = pers.getW();

        // Возраст считается на дату начала госпитализации (МР стр. 164)
        final LocalDate dr = pers.getDr();
        final LocalDate date1 = sl.getDate1();

        final List<String> codeUslList = sl.getUslList() == null ? List.of() :
                sl.getUslList().stream().map(Usl::getCodeUsl).toList();

        final List<String> critList = sl.getKsgKpg() == null || sl.getKsgKpg().getCritList() == null ? List.of() :
                sl.getKsgKpg().getCritList();

        return ksgGrouperRepo.getGrouper(uslOk).stream()
                .filter(g -> isDsMatch(ds1, g.getDs1()))
                .filter(g -> isAnyDsMatch(ds2List, g.getDs2()))
                .filter(g -> isAnyDsMatch(ds3List, g.getDs3()))
                .filter(g -> isCodeUslMatch(codeUslList, g.getCodeUsl()))
                .filter(g -> isDkkMatch(critList, g.getDkk()))
                .filter(g -> isFrakMatch(critList, g.getFrak()))
                .filter(g -> isAgeMatch(dr, date1, g.getAge()))
                .filter(g -> isKdMatch(kd, g.getKd()))
                .filter(g -> g.getSex() == null || g.getSex() == sex)
                .toList();
    }
}
