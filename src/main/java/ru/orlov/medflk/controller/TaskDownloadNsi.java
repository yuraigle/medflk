package ru.orlov.medflk.controller;


import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.service.NsiDownloaderService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskDownloadNsi extends Task<Void> {

    private final NsiDownloaderService nsiDownloaderService;

    @Override
    protected Void call() throws Exception {
        updateMessage("Загрузка справочников с сайта ФФОМС...");

        List<String> packages = nsiDownloaderService.getFfomsPackages();
        packages.forEach(p -> {
            updateMessage("Загрузка справочника " + p + "...");
            nsiDownloaderService.updateFfoms(p);
        });

        packages = nsiDownloaderService.getRmzPackages().keySet().stream().toList();
        packages.forEach(p -> {
            updateMessage("Загрузка справочника " + p + "...");
            nsiDownloaderService.updateRmz(p);
        });

        updateMessage("");

        return null;
    }
}
