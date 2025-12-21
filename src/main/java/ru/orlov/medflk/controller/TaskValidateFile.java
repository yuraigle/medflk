package ru.orlov.medflk.controller;

import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.service.FileValidatorService;

import java.io.File;

@Component
@RequiredArgsConstructor
public class TaskValidateFile extends Task<ValidationResult> {

    private final FileValidatorService validator;

    @Setter
    private File file;

    @Override
    protected ValidationResult call() {
        updateMessage("Проверяем файл " + file.getName());
        ValidationResult procLog = validator.validate(file);
        updateMessage("");

        System.out.println("...");

        return procLog;
    }


}
