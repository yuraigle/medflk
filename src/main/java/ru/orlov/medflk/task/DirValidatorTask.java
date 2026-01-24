package ru.orlov.medflk.task;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.service.FileValidatorService;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirValidatorTask {

    private final FileValidatorService validator;
    public static final List<File> queue = new ArrayList<>();

    public Task<Void> getTaskWithStatus(StringProperty status) {
        return new Task<>() {

            @Override
            protected Void call() throws Exception {
                while (true) {
                    if (!queue.isEmpty()) {
                        File file = queue.getFirst();

                        appendLine("Проверяем файл " + file.getName());
                        validator.validate(file);
                        appendLine("Проверка завершена");
                    }
                }
            }

            private void appendLine(String line) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                String dt = LocalDateTime.now().format(fmt);

                String s = status.get() + dt + "  " + line + "\n";
                status.set(s);
            }
        };
    }

}
