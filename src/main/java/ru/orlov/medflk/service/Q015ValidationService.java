package ru.orlov.medflk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.controller.CheckTabController;
import ru.orlov.medflk.domain.CheckFact;
import ru.orlov.medflk.domain.nsi.Q015Packet;
import ru.orlov.medflk.domain.nsi.Q015Service;
import ru.orlov.medflk.jaxb.*;
import ru.orlov.medflk.q015.AbstractCheck;

import java.time.LocalDate;
import java.util.*;

import static ru.orlov.medflk.Utils.castList;
import static ru.orlov.medflk.Utils.getZlListMdType;

@Log4j2
@Service
@RequiredArgsConstructor
public class Q015ValidationService {

    // все персоны из L-файла уходят в кэш для ускорения поиска по ним
    private static final Map<String, Pers> persCache = new HashMap<>();
    private final Q015Service q015Service;

    public static Pers getPersById(String idPac) {
        return persCache.getOrDefault(idPac, null);
    }

    public void validate(ZlList zlList, PersList persList, FlkP flkP) {
        String zlType = getZlListMdType(zlList);

        // список проверок Q015 берём на дату счёта
        List<Q015Packet.Q015> q015List = new ArrayList<>();
        LocalDate q15Date = zlList.getSchet().getDschet();
        q015List.addAll(q015Service.getChecksForType(zlType, q15Date));
        q015List.addAll(q015Service.getChecksForType("L", q15Date));
        q015List.sort(Comparator.comparing(Q015Packet.Q015::getIdTest));

        // debug
        q015List = q015List.stream()
                .filter(q -> q.getIdTest().startsWith("002"))
                .toList();

        persCache.clear();
        persList.getPersList().forEach(pers -> persCache.put(pers.getIdPac(), pers));

        for (Q015Packet.Q015 check : q015List) {
            CheckFact fact = CheckFact.builder().test(check.getIdTest())
                    .element(check.getIdEl()).result("").build();

            if (check.getBean() != null && check.getBean() instanceof AbstractCheck) {
                fact.setDescription(((AbstractCheck) check.getBean()).getErrorMessage());

                try {
                    List<FlkErr> e1 = applyCheck(check, zlList, persList);
                    if (e1 != null && !e1.isEmpty()) {
                        fact.setResult(e1.size() + " ошибок");
                        flkP.getPrList().addAll(e1);
                    } else {
                        fact.setResult("OK");
                    }
                } catch (Exception e) {
                    fact.setResult("Ошибка обработки");
                }
            } else {
                fact.setDescription("Проверка не реализована");
            }

            CheckTabController.checkFactList.add(fact);
        }
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
