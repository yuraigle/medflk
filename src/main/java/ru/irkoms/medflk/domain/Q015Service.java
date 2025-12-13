package ru.irkoms.medflk.domain;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;
import ru.irkoms.medflk.q015.AbstractCheck;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Setter
@Service
@RequiredArgsConstructor
public class Q015Service extends AbstractNsiService {

    private final ApplicationContext ctx;
    private Q015Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(Q015Packet.class, "nsi/Q015.ZIP");
        attachCheckerBeans();
    }

    public List<Q015Packet.Q015> getChecksForType(String type, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(q015 -> q015.getTypeMd().getTypeD().contains(type))
                .filter(q015 -> !q015.getDatebeg().isAfter(d1))
                .filter(q015 -> q015.getDateend() == null || q015.getDateend().isAfter(d1))
                .toList();
    }

    /**
     * Для каждой проверки пытаемся найти класс и метод, который её реализует
     */
    public void attachCheckerBeans() {
        List<Object> allCheckers = new ArrayList<>();
        Arrays.stream(ctx.getBeanDefinitionNames())
                .filter(s -> s.toLowerCase().contains("check_"))
                .forEach(s -> allCheckers.add(ctx.getBean(s)));

        for (Object bean : allCheckers) {
            if (bean instanceof AbstractCheck) {
                Method method = ReflectionUtils.findMethod(bean.getClass(),
                        "check", ZlList.class, PersList.class);

                for (Q015Packet.Q015 check : packet.getZapList()) {
                    String checkId = "Check_" + check.getIdTest().replaceAll("\\.", "_");
                    if (bean.getClass().getSimpleName().equals(checkId)) {
                        check.setBean(bean);
                        check.setMethod(method);
                    }
                }
            }
        }
    }
}
