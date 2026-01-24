package ru.orlov.medflk.task;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.service.FileValidatorService;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.orlov.medflk.Utils.pluralErr;
import static ru.orlov.medflk.Utils.waitForFileUnlock;

@Service
@RequiredArgsConstructor
public class DirValidatorTask {

    private final FileValidatorService validator;
    public static final List<File> queue = new ArrayList<>();

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public Task<Void> getTaskWithStatus(StringProperty status) {
        return new Task<>() {

            @Override
            protected Void call() throws Exception {
                while (true) {
                    if (!queue.isEmpty()) {
                        File file = queue.getFirst();
                        waitForFileUnlock(file, 30000);

                        FlkP flkP = validator.validate(file);
                        int cntErr = flkP.getPrList() == null ? 0 : flkP.getPrList().size();
                        appendLine(String.format("Проверен файл %s. %s",
                                file.getName(), cntErr == 0 ? "OK" : pluralErr(cntErr)));

                        queue.remove(file);
                    }
                }
            }

            private void appendLine(String line) {
                String dt = LocalDateTime.now().format(fmt);

                String ss = status.get() == null ? "" : status.get();
                String s = ss + dt + "  " + line + "\n";
                status.set(s);
            }
        };
    }
}
