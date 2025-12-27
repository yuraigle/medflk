package ru.orlov.medflk.service;

import javafx.animation.PauseTransition;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.util.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.domain.NsiRow;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class NsiInitializerTask {

    private final ApplicationContext ctx;

    @Getter
    private final ObservableList<NsiRow> observableClassifiers =
            FXCollections.observableList(new ArrayList<>());

    private static Boolean isRunning = false;

    public Task<Void> getTaskWithStatus(StringProperty status) {
        if (isRunning) {
            return null;
        }

        return new Task<>() {
            @Override
            protected Void call() {
                observableClassifiers.clear();

                List<String> nsiServices = Arrays.stream(ctx.getBeanDefinitionNames())
                        .filter(name -> name.matches("^.*[a-zA-Z][0-9]{3}Service$"))
                        .toList();

                int ttl = nsiServices.size();
                AtomicInteger cntReady = new AtomicInteger(0);

                nsiServices.forEach(name -> {
                    updateMessage("Инициализируем справочники " + cntReady.incrementAndGet() + "/" + ttl);
                    Object bean = ctx.getBean(name);
                    if (bean instanceof AbstractNsiService nsi) {
                        nsi.initPacket();

                        NsiRow row = new NsiRow();
                        row.setCode(nsi.getClass().getSimpleName().substring(0, 4));
                        row.setDate(nsi.getDate());
                        row.setVersion(nsi.getVersion());
                        row.setDescription(nsi.getDescription());

                        observableClassifiers.add(row);
                    }
                });
                return null;
            }

            @Override
            protected void scheduled() {
                isRunning = true;
                status.bind(messageProperty());
            }

            @Override
            protected void succeeded() {
                isRunning = false;
                status.unbind();
                showStatusMessage(status, "Справочники инициализированы");
            }

            @Override
            protected void failed() {
                isRunning = false;
                status.unbind();
                showStatusMessage(status, "Ошибка при загрузке справочников: " + getException().getMessage());
            }

            @Override
            protected void cancelled() {
                isRunning = false;
                status.unbind();
                showStatusMessage(status, "Задача отменена");
            }
        };
    }

    private void showStatusMessage(StringProperty status, String message) {
        status.set(message);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(ev -> {
            if (!status.isBound()) {
                status.set("");
            }
        });
        pause.play();
    }

}
