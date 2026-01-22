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
import java.util.regex.Pattern;

@Log4j2
@Service
@RequiredArgsConstructor
public class FileValidatorService {

    private final RegistryReaderService registryReaderService;
    private final SchemaValidationService schemaValidationService;
    private final Q015ValidationService q015ValidationService;

    Pattern hFile = Pattern.compile("^([CHT]|D[A-Z])[MST][0-9]{2,6}[MST][0-9]{2,6}_[0-9]{4}.*$");
    Pattern lFile = Pattern.compile("^L[A-Z]?[MST][0-9]{2,6}[MST][0-9]{2,6}_[0-9]{4}.*$");

    public FlkP validate(File zip) {
        String fileType = zip.getName().substring(0, 1).toUpperCase();

        FlkP flkP = new FlkP();
        ZlList zlList = null;
        PersList persList = null;

        try {
            if (!hFile.matcher(zip.getName()).matches()) {
                throw new Exception("Имя файла не соответствует шаблону");
            }

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
        } catch (Exception e) {
            CheckFact fact0 = CheckFact.builder().test("001T.00.0000")
                    .description(e.getMessage()).result("Ошибка").build();
            flkP.getPrList().add(new FlkErr(fact0.getTest(), fact0.getDescription()));
            CheckTabController.checkFactList.add(fact0);
        }


        if (zlList != null && persList != null && flkP.getPrList().isEmpty()) {
            q015ValidationService.validate(zlList, persList, flkP);
        }

        return flkP;
    }

}
