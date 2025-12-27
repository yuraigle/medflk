package ru.orlov.medflk.task;

import javafx.animation.PauseTransition;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.domain.NsiRow;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;
import ru.orlov.medflk.service.NsiInitializerService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Service
@RequiredArgsConstructor
public class NsiInitializerTask {

    private final NsiInitializerService nsiInitializerService;

    private static Boolean isRunning = false;

    public Task<Void> getTaskWithStatus(StringProperty status) {
        if (isRunning) {
            return null;
        }

        return new Task<>() {
            @Override
            protected Void call() {
                List<String> nsiServices = nsiInitializerService.getAllNsiServices();

                nsiInitializerService.getObservableClassifiers().clear();

                int ttl = nsiServices.size();
                AtomicInteger cntReady = new AtomicInteger(0);

                nsiServices.forEach(name -> {
                    updateMessage("Инициализируем справочники " + cntReady.incrementAndGet() + "/" + ttl);
                    String code = name.substring(0, 4).toUpperCase();
                    AbstractNsiService nsi = nsiInitializerService.initializeNsiService(code);
                    nsiInitializerService.getObservableClassifiers().add(new NsiRow(nsi));
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
                showStatusMessage(status, "Ошибка инициализации справочника: " + getException().getMessage());
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
