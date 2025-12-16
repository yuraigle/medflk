package ru.orlov.medflk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.domain.nsi.Q015Packet;
import ru.orlov.medflk.domain.nsi.Q015Service;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.Zap;
import ru.orlov.medflk.jaxb.ZlList;
import ru.orlov.medflk.q015.AbstractCheck;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static ru.orlov.medflk.Utils.castList;
import static ru.orlov.medflk.Utils.getZlListMdType;

@Log4j2
@Service
@RequiredArgsConstructor
public class Q015ValidationService {

    // все персоны из L-файла уходят в кэш для ускорения поиска по ним
    private static final Map<String, PersList.Pers> persCache = new HashMap<>();
    private final Q015Service q015Service;

    public static PersList.Pers getPersById(String idPac) {
        return persCache.getOrDefault(idPac, null);
    }

    public void validate(ZlList zlList, PersList persList, ValidationResult proc) {
        String zlType = getZlListMdType(zlList);

        // список проверок Q015 берём на дату счёта
        List<Q015Packet.Q015> q015List = new ArrayList<>();
        LocalDate q15Date = zlList.getSchet().getDschet();
        q015List.addAll(q015Service.getChecksForType(zlType, q15Date));
        q015List.addAll(q015Service.getChecksForType("L", q15Date));
        q015List.sort(Comparator.comparing(Q015Packet.Q015::getIdTest));

        persCache.clear();
        for (Zap zap : zlList.getZapList()) {
            PersList.Pers pers = persList.getPersList().stream()
                    .filter(p -> p.getIdPac().equals(zap.getPacient().getIdPac()))
                    .findFirst()
                    .orElse(null);
            persCache.put(zap.getPacient().getIdPac(), pers);
        }

        q015List = q015List.stream()
                .filter(q -> q.getIdTest().startsWith("001"))
                .toList();

        int cntActiveChecks = 0;
        for (Q015Packet.Q015 check : q015List) {
            ValidationResult.Line line = new ValidationResult.Line(check.getIdTest(), null);
            if (check.getBean() != null && check.getBean() instanceof AbstractCheck) {
                cntActiveChecks++;
                try {
                    List<FlkErr> e1 = applyCheck(check, zlList, persList);
                    if (e1 != null && !e1.isEmpty()) {
                        String comment = e1.getFirst().getComment();
                        line.setComment(comment + " (" + e1.size() + " ошибок)");
                        proc.getErrors().addAll(e1);
                        proc.setDeclined(true);
                    } else {
                        line.setComment("OK");
                    }
                } catch (Exception e) {
                    line.setComment("Ошибка обработчика при проведении проверки: " + e.getMessage());
                    proc.setDeclined(true);
                }
            } else {
                line.setComment(String.format("Проверка %s не реализована", check.getIdEl()));
            }
            proc.getLines().add(line);
        }

        proc.setEndedAt(LocalDateTime.now());
        proc.addLine(String.format("%s / %s проверок выполнено", cntActiveChecks, q015List.size()));
    }

    private List<FlkErr> applyCheck(Q015Packet.Q015 q015, Object a, Object b) throws Exception {
        long startedAt = System.nanoTime();
        List<FlkErr> errors = new ArrayList<>();

        Object result = q015.getMethod().invoke(q015.getBean(), a, b);
        if (result != null) {
            errors = castList(FlkErr.class, (List<?>) result);
            errors.forEach(e -> e.fillFromQ015(q015));
        }

        long executionTimeMs = (System.nanoTime() - startedAt) / 1_000_000;
        if (executionTimeMs > 100) {
            log.warn("Check {} took too long: {}ms", q015.getIdTest(), executionTimeMs);
        }

        return errors;
    }
}
