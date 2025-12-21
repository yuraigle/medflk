package ru.orlov.medflk.controller;


import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.service.NsiDownloaderService;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class TaskDownloadNsi extends Task<Void> {

    private final NsiDownloaderService nsiDownloaderService;

    @Override
    protected Void call() {
        List<String> packages1 = nsiDownloaderService.getFfomsPackages();
        List<String> packages2 = nsiDownloaderService.getRmzPackages().keySet().stream().toList();
        int ttl = packages1.size() + packages2.size();
        AtomicInteger count = new AtomicInteger(0);

        packages1.forEach(p -> {
            updateMessage("Скачиваем справочники " + count.incrementAndGet() + "/" + ttl);
            nsiDownloaderService.updateFfoms(p);
        });

        packages2.forEach(p -> {
            updateMessage("Скачиваем справочники " + count.incrementAndGet() + "/" + ttl);
            nsiDownloaderService.updateRmz(p);
        });

        updateMessage("");

        return null;
    }
}
