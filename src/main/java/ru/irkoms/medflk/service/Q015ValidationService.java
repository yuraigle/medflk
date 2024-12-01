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
import ru.irkoms.medflk.q015.AbstractCheckZapWithPers;

import java.util.ArrayList;
import java.util.List;

import static ru.irkoms.medflk.Utils.castList;
import static ru.irkoms.medflk.Utils.getZlListMdType;

@Log4j2
@Service
@RequiredArgsConstructor
public class Q015ValidationService {

    private final Q015Service q015Service;

    public List<FlkP.Pr> validate(AZlList zlList, APersList persList) {
        String zlType = getZlListMdType(zlList);

        List<Q015Packet.Q015> q015List = new ArrayList<>();
        q015List.addAll(q015Service.getChecksForType(zlType));
        q015List.addAll(q015Service.getChecksForType("L"));

        List<FlkP.Pr> errors = new ArrayList<>();

        // для этих тестов можно для каждого пробежать по всему дереву
        for (Q015Packet.Q015 check : q015List) {
            if (check.getBean() != null && check.getBean() instanceof AbstractCheck) {
                errors.addAll(applyCheck(check, zlList, persList));
            }
        }

        // для этих тестов надо находить персону из L-файла, делаем это 1 раз
        for (AZap zap : zlList.getZapList()) {
            APers pers = getPersById(persList, zap.getPacient().getIdPac());
            if (pers == null) {
                continue;
            }

            for (Q015Packet.Q015 check : q015List) {
                if (check.getBean() != null && check.getBean() instanceof AbstractCheckZapWithPers) {
                    errors.addAll(applyCheck(check, zap, pers));
                }
            }
        }

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
        }

        long executionTimeMs = (System.nanoTime() - startedAt) / 1_000_000;
        if (executionTimeMs > 100) {
            log.warn("Check {} took too long: {}ms", q015.getIdTest(), executionTimeMs);
        }

        return errors;
    }

    private APers getPersById(APersList persList, String idPac) {
        return persList.getPersList().stream()
                .filter(p -> p.getIdPac().equals(idPac))
                .findFirst()
                .orElse(null);
    }
}
