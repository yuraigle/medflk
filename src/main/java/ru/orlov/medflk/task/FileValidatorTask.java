package ru.orlov.medflk.task;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.service.FileValidatorService;

import java.io.File;

import static ru.orlov.medflk.service.StatusService.showStatusMessage;

@Service
@RequiredArgsConstructor
public class FileValidatorTask {

    private final FileValidatorService validator;

    public Task<ValidationResult> getTaskWithStatus(File file, StringProperty status) {
        return new Task<>() {

            @Override
            protected ValidationResult call() {
                updateMessage("Проверяем файл " + file.getName());
                ValidationResult procLog = validator.validate(file);
                updateMessage("");

                return procLog;
            }

            @Override
            protected void scheduled() {
                status.bind(messageProperty());
            }

            @Override
            protected void succeeded() {
                status.unbind();
            }

            @Override
            protected void failed() {
                status.unbind();
                showStatusMessage(status, "Ошибка проверки файла " +
                        file.getName() + " : " + getException().getMessage());
            }

            @Override
            protected void cancelled() {
                status.unbind();
                showStatusMessage(status, "Задача отменена");
            }
        };
    }
}
