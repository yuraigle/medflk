package ru.irkoms.medflk.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;
import ru.irkoms.medflk.q015.AbstractCheck;
import ru.irkoms.medflk.service.NsiReaderService;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class Q015Service {

    private final ApplicationContext ctx;
    private final List<Q015Packet.Q015> q015List = new ArrayList<>();

    public List<Q015Packet.Q015> getChecks() {
        if (q015List.isEmpty()) {
            fillFromNsi();
            attachCheckerBeans();
        }

        return q015List;
    }

    public List<Q015Packet.Q015> getChecksForType(String type) {
        LocalDate d1 = LocalDate.now();
        return getChecks().stream()
                .filter(q015 -> q015.getTypeMd().getTypeD().contains(type))
                .filter(q015 -> !q015.getDatebeg().isAfter(d1))
                .filter(q015 -> q015.getDateend() == null || q015.getDateend().isAfter(d1))
                .toList();
    }

    public Optional<Q015Packet.Q015> getCheckById(String id) {
        LocalDate d1 = LocalDate.now();
        return getChecks().stream()
                .filter(q015 -> q015.getIdTest().equals(id))
                .filter(q015 -> !q015.getDatebeg().isAfter(d1))
                .filter(q015 -> q015.getDateend() == null || q015.getDateend().isAfter(d1))
                .findFirst();
    }

    private void fillFromNsi() {
        try {
            Q015Packet packet = NsiReaderService.readNsi(Q015Packet.class, "nsi/Q015.ZIP");
            q015List.addAll(packet.getQ015List());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void attachCheckerBeans() {
        List<Object> allCheckers = new ArrayList<>();
        Arrays.stream(ctx.getBeanDefinitionNames())
                .filter(s -> s.contains("check_"))
                .forEach(s -> allCheckers.add(ctx.getBean(s)));

        for (Q015Packet.Q015 check : q015List) {
            String className = "Check_" + check.getIdTest().replaceAll("\\.", "_");

            for (Object bean : allCheckers) {
                if (!bean.getClass().getSimpleName().equalsIgnoreCase(className)) {
                    continue;
                }

                check.setBean(bean);
                if (bean instanceof AbstractCheck) {
                    Method method = ReflectionUtils.findMethod(bean.getClass(), "check",
                            AZlList.class, APersList.class);
                    check.setMethod(method);
                }
            }
        }
    }
}
