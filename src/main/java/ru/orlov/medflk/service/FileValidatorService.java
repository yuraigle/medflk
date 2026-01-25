package ru.orlov.medflk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.controller.CheckTabController;
import ru.orlov.medflk.domain.CheckFact;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.io.File;

import static ru.orlov.medflk.Utils.rxFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileValidatorService {

    private final RegistryReaderService registryReaderService;
    private final SchemaValidationService schemaValidationService;
    private final Q015ValidationService q015ValidationService;

    public FlkP validate(File zip, boolean verbose) {
        FlkP flkP = new FlkP(zip.getName());

        if (verbose) {
            CheckTabController.checkFactList.clear();
        }

        try {
            if (!rxFile.matcher(zip.getName()).matches()) {
                throw new Exception("Имя файла не соответствует шаблону");
            }

            ZlList zlList;
            PersList persList;
            String fileType = zip.getName().substring(0, 1).toUpperCase();

            try {
                zlList = registryReaderService.parseZlList(zip);
            } catch (Exception e) {
                throw new Exception(fileType + "-файл должен быть валидным XML");
            }

            if (zlList.getZapList() == null || zlList.getZapList().isEmpty()) {
                throw new Exception(fileType + "-файл должен содержать записи");
            }

            if (!schemaValidationService.validate(zlList).isEmpty()) {
                throw new Exception(fileType + "-файл не должен содержать ошибок схемы");
            }

            try {
                persList = registryReaderService.parsePersList(zip);
            } catch (Exception e) {
                throw new Exception("L-файл должен быть валидным XML");
            }

            if (persList.getPersList() == null || persList.getPersList().isEmpty()) {
                throw new Exception("L-файл должен содержать записи");
            }

            if (!schemaValidationService.validate(persList).isEmpty()) {
                throw new Exception("L-файл не должен содержать ошибок схемы");
            }

            q015ValidationService.validate(zlList, persList, flkP, verbose);
        } catch (Exception e) {
            CheckFact fact0 = CheckFact.builder().test("001T.00.0000")
                    .description(e.getMessage()).result("Ошибка").build();
            flkP.getPrList().add(new FlkErr(fact0.getTest(), fact0.getDescription()));

            if (verbose) {
                CheckTabController.checkFactList.add(fact0);
            }
        }

        return flkP;
    }

}
