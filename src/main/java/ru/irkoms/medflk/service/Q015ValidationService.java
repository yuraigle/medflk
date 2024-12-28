package ru.irkoms.medflk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.irkoms.medflk.domain.Q015Packet;
import ru.irkoms.medflk.domain.Q015Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZap;
import ru.irkoms.medflk.jaxb.meta.AZlList;
import ru.irkoms.medflk.q015.AbstractCheck;

import java.util.*;

import static ru.irkoms.medflk.Utils.castList;
import static ru.irkoms.medflk.Utils.getZlListMdType;

@Log4j2
@Service
@RequiredArgsConstructor
public class Q015ValidationService {

    private final Q015Service q015Service;

    // все персоны из L-файла уходят в кэш для ускорения поиска по ним
    private static final Map<String, APers> persCache = new HashMap<>();

    public static APers getPersById(String idPac) {
        return persCache.getOrDefault(idPac, null);
    }

    public List<FlkP.Pr> validate(AZlList zlList, APersList persList) {
        String zlType = getZlListMdType(zlList);

        List<Q015Packet.Q015> q015List = new ArrayList<>();
        q015List.addAll(q015Service.getChecksForType(zlType));
        q015List.addAll(q015Service.getChecksForType("L"));
        q015List.sort(Comparator.comparing(Q015Packet.Q015::getIdTest));

        persCache.clear();
        for (AZap zap : zlList.getZapList()) {
            APers pers = persList.getPersList().stream()
                    .filter(p -> p.getIdPac().equals(zap.getPacient().getIdPac()))
                    .findFirst()
                    .orElse(null);
            persCache.put(zap.getPacient().getIdPac(), pers);
        }

        List<FlkP.Pr> errors = new ArrayList<>();
        int cntActiveChecks = 0;
        for (Q015Packet.Q015 check : q015List) {
            if (check.getBean() != null && check.getBean() instanceof AbstractCheck) {
                cntActiveChecks++;
                errors.addAll(applyCheck(check, zlList, persList));
            }
        }
        log.info("{} / {} checks applied", cntActiveChecks, q015List.size());

        return errors;
    }

    private List<FlkP.Pr> applyCheck(Q015Packet.Q015 q015, Object a, Object b) {
        long startedAt = System.nanoTime();
        List<FlkP.Pr> errors = new ArrayList<>();

        try {
            Object result = q015.getMethod().invoke(q015.getBean(), a, b);
            if (result != null) {
                errors = castList(FlkP.Pr.class, (List<?>) result);
                errors.forEach(e -> e.fillFromQ015(q015));
            }
        } catch (Exception e) {
            log.error("Error while applying check {}: {}", q015.getIdTest(), e.getMessage());
            e.printStackTrace();
        }

        long executionTimeMs = (System.nanoTime() - startedAt) / 1_000_000;
        if (executionTimeMs > 100) {
            log.warn("Check {} took too long: {}ms", q015.getIdTest(), executionTimeMs);
        }

        return errors;
    }
}
