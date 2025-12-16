package ru.orlov.medflk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.domain.ValidationResult;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.io.File;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileValidatorService {

    private final RegistryReaderService registryReaderService;
    private final SchemaValidationService schemaValidationService;
    private final Q015ValidationService q015ValidationService;

    public ValidationResult validate(File zip) {
        ValidationResult procLog = new ValidationResult(zip);

        ZlList zlList;
        PersList persList;

        try {
            zlList = registryReaderService.parseZlList(zip);
            persList = registryReaderService.parsePersList(zip);
        } catch (Exception e) {
            procLog.addError(String.format("Ошибка чтения файла " + e.getMessage()));
            return procLog;
        }

        if (zlList == null || persList == null) {
            procLog.addError("Файл не содержит записей");
            return procLog;
        }

        List<FlkErr> schemaErrors = schemaValidationService.validate(zlList);
        schemaErrors.addAll(schemaValidationService.validate(persList));
        if (!schemaErrors.isEmpty()) {
            for (FlkErr error : schemaErrors) {
                procLog.addFlkError(error);
            }
            procLog.addError("Файл содержит ошибки схемы, дальнейшие проверки не проводятся");
            return procLog;
        }

        q015ValidationService.validate(zlList, persList, procLog);

        return procLog;
    }

}
