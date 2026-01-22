package ru.orlov.medflk.task;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.domain.NsiRow;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;
import ru.orlov.medflk.service.NsiInitializerService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.orlov.medflk.service.NsiInitializerService.isNsiReady;
import static ru.orlov.medflk.service.StatusService.showStatusMessage;

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

                isNsiReady.set(false);

                NsiInitializerService.classifiers.clear();

                int ttl = nsiServices.size();
                AtomicInteger cntReady = new AtomicInteger(0);

                nsiServices.forEach(name -> {
                    updateMessage("Инициализируем справочники " + cntReady.incrementAndGet() + "/" + ttl);
                    String code = name.substring(0, 4).toUpperCase();
                    AbstractNsiService nsi = nsiInitializerService.initializeNsiService(code);
                    NsiInitializerService.classifiers.add(new NsiRow(nsi));
                });

                isNsiReady.set(true);

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
}
