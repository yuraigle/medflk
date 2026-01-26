package ru.orlov.medflk.task;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.*;
import java.util.List;
import java.util.regex.Pattern;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static ru.orlov.medflk.Utils.waitForFileUnlock;

@Service
@RequiredArgsConstructor
public class DirMonitoringTask {

    private final Pattern rxFile = Pattern
            .compile("^([CHT]|D[A-Z])[MST][0-9]{2,6}[MST][0-9]{2,6}_[0-9]{4}.*\\.ZIP$");

    public static final ListProperty<File> queue =
            new SimpleListProperty<>(FXCollections.observableArrayList());

    public Task<Void> getTask(File dirIn) {
        return new Task<>() {

            @Override
            protected Void call() throws Exception {
                // добавить существующие файлы в очередь
                List<File> files = FileUtils
                        .listFiles(dirIn, null, false).stream()
                        .filter(f -> rxFile.matcher(f.getName().toUpperCase()).matches())
                        .toList();
                queue.addAll(files);

                // ожидать новых файлов
                WatchService watchService = FileSystems.getDefault().newWatchService();
                dirIn.toPath().register(watchService, ENTRY_CREATE);
                boolean poll = true;

                while (poll) {
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> ev : key.pollEvents()) {
                        String fileName = ev.context().toString();
                        if (rxFile.matcher(fileName).matches()) {
                            File file = Paths.get(dirIn.getAbsolutePath(), fileName).toFile();
                            waitForFileUnlock(file, 30000);
                            queue.add(file);
                        }
                    }

                    poll = key.reset();
                }

                return null;
            }
        };
    }
}
