package ru.orlov.medflk.service;

import javafx.animation.PauseTransition;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class NsiDownloaderTask {

    private final NsiDownloaderService nsiDownloaderService;

    private static Boolean isRunning = false;

    public Task<Void> getTaskWithStatus(StringProperty status) {
        if (isRunning) {
            return null;
        }

        return new Task<>() {
            @Override
            protected Void call() {
                List<String> packages1 = nsiDownloaderService.getFfomsPackages();
                List<String> packages2 = nsiDownloaderService.getRmzPackages().keySet().stream().toList();
                int ttl = packages1.size() + packages2.size();
                AtomicInteger cntReady = new AtomicInteger(0);

                packages1.forEach(p -> {
                    updateMessage("Скачиваем справочники " + cntReady.incrementAndGet() + "/" + ttl);
                    nsiDownloaderService.updateFfoms(p);
                });

                packages2.forEach(p -> {
                    updateMessage("Скачиваем справочники " + cntReady.incrementAndGet() + "/" + ttl);
                    nsiDownloaderService.updateRmz(p);
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
