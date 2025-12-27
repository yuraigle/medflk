package ru.orlov.medflk.task;

import javafx.animation.PauseTransition;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.NsiRow;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;
import ru.orlov.medflk.service.NsiDownloaderService;
import ru.orlov.medflk.service.NsiInitializerService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class NsiDownloaderTask {

    private final ApplicationContext ctx;
    private final NsiInitializerService nsiInitializerService;
    private final NsiDownloaderService nsiDownloaderService;

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

                AtomicInteger cntReady = new AtomicInteger(0);
                int ttl = nsiServices.size();
                nsiServices.forEach(name -> {
                    Object bean = ctx.getBean(name);
                    if (bean instanceof AbstractNsiService nsi) {
                        String code = nsi.getClass().getSimpleName().substring(0, 4).toUpperCase();
                        updateMessage("Скачиваем справочники " + cntReady.incrementAndGet() + "/" + ttl);

                        if (nsiDownloaderService.getRmzPackages().containsKey(code)) {
                            nsiDownloaderService.updateRmz(code);
                        } else {
                            nsiDownloaderService.updateFfoms(code);
                        }
                        nsi.initPacket();
                        nsiInitializerService.getObservableClassifiers().add(new NsiRow(nsi));
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
                showStatusMessage(status, "Справочники обновлены");
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
