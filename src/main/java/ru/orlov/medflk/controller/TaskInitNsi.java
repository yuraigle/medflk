package ru.orlov.medflk.controller;


import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class TaskInitNsi extends Task<Void> {

    private final ApplicationContext ctx;

    @Override
    protected Void call() throws Exception {
        updateMessage("Инициализация справочников");

        List<String> nsiServices = Arrays.stream(ctx.getBeanDefinitionNames())
                .filter(name -> name.matches("^.*[a-zA-Z][0-9]{3}Service$"))
                .toList();

        AtomicInteger cntReady = new AtomicInteger(0);
        nsiServices.forEach(name -> {
            Object bean = ctx.getBean(name);
            if (bean instanceof AbstractNsiService) {
                ((AbstractNsiService) bean).initPacket();
            }
            updateMessage("Инициализация справочников " +
                    cntReady.incrementAndGet() + "/" + nsiServices.size());
        });

        updateMessage("");

        return null;
    }
}
