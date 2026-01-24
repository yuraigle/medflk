package ru.orlov.medflk.task;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@Service
@RequiredArgsConstructor
public class DirMonitoringTask {

    private String dir = "D:\\MED\\INBOX";
    private String out = "D:\\MED\\INBOX\\1";

//    private List<File> files = new ArrayList<>();

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final Pattern rxFile = Pattern
            .compile("^([CHT]|D[A-Z])[MST][0-9]{2,6}[MST][0-9]{2,6}_[0-9]{4}.*\\.ZIP$");

    public Task<Void> getTaskWithStatus(StringProperty status) {
        return new Task<>() {

            @Override
            protected Void call() throws Exception {
                List<File> files = FileUtils
                        .listFiles(new File(dir), null, false).stream()
                        .filter(f -> rxFile.matcher(f.getName().toUpperCase()).matches())
                        .toList();
                DirValidatorTask.queue.addAll(files);

                WatchService watchService = FileSystems.getDefault().newWatchService();
                Paths.get(dir).register(watchService, ENTRY_CREATE);
                appendLine("Запущен мониторинг папки " + dir);

                boolean poll = true;

                while (poll) {
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind().equals(ENTRY_CREATE)) {
                            File file = Paths.get(dir, event.context().toString()).toFile();
                            DirValidatorTask.queue.add(file);

                            appendLine("Новый файл: " + event.context());
                        }
                    }

                    poll = key.reset();
                }

                return null;
            }

            @Override
            protected void succeeded() {
                appendLine("Мониторинг остановлен");
            }

            @Override
            protected void failed() {
                appendLine("Мониторинг остановлен");
            }

            @Override
            protected void cancelled() {
                appendLine("Мониторинг остановлен");
            }

            private void appendLine(String line) {
                String dt = LocalDateTime.now().format(fmt);

                String s = status.get() + dt + "  " + line + "\n";
                status.set(s);
            }
        };
    }

}
