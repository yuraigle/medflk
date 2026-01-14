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

@Log4j2
@Service
@RequiredArgsConstructor
public class FileValidatorService {

    private final RegistryReaderService registryReaderService;
    private final SchemaValidationService schemaValidationService;
    private final Q015ValidationService q015ValidationService;

    public FlkP validate(File zip) {
        String fileType = zip.getName().substring(0, 1).toUpperCase();

        FlkP flkP = new FlkP();
        ZlList zlList = null;
        PersList persList = null;

        CheckFact fact1 = CheckFact.builder().test("001T.00.0001")
                .description("L-файл должен быть валидным XML").build();
        try {
            persList = registryReaderService.parsePersList(zip);
            fact1.setResult("OK");
        } catch (Exception ex) {
            fact1.setResult("Ошибка");
            flkP.getPrList().add(new FlkErr(fact1.getTest(), fact1.getDescription()));
        }
        CheckTabController.checkFactList.add(fact1);

        CheckFact fact2 = CheckFact.builder().test("001T.00.0002")
                .description("L-файл должен содержать записи").build();
        if (persList != null && persList.getPersList() != null && !persList.getPersList().isEmpty()) {
            fact2.setResult("OK");
        } else {
            fact2.setResult("Ошибка");
            flkP.getPrList().add(new FlkErr(fact2.getTest(), fact2.getDescription()));
        }
        CheckTabController.checkFactList.add(fact2);

        CheckFact fact3 = CheckFact.builder().test("001T.00.0003")
                .description("L-файл не должен содержать ошибок схемы").build();
        if (schemaValidationService.validate(persList).isEmpty()) {
            fact3.setResult("OK");
        } else {
            fact3.setResult("Ошибка");
            flkP.getPrList().add(new FlkErr(fact3.getTest(), fact3.getDescription()));
        }
        CheckTabController.checkFactList.add(fact3);

        CheckFact fact4 = CheckFact.builder().test("001T.00.0004")
                .description(fileType + "-файл должен быть валидным XML").build();
        try {
            zlList = registryReaderService.parseZlList(zip);
            fact4.setResult("OK");
        } catch (Exception e) {
            fact4.setResult("Ошибка");
            flkP.getPrList().add(new FlkErr(fact4.getTest(), fact4.getDescription()));
        }
        CheckTabController.checkFactList.add(fact4);

        CheckFact fact5 = CheckFact.builder().test("001T.00.0005")
                .description(fileType + "-файл должен содержать записи").build();
        if (zlList != null && zlList.getZapList() != null && !zlList.getZapList().isEmpty()) {
            fact5.setResult("OK");
        } else {
            fact5.setResult("Ошибка");
            flkP.getPrList().add(new FlkErr(fact5.getTest(), fact5.getDescription()));
        }
        CheckTabController.checkFactList.add(fact5);

        CheckFact fact6 = CheckFact.builder().test("001T.00.0006")
                .description(fileType + "-файл не должен содержать ошибок схемы").build();
        if (schemaValidationService.validate(persList).isEmpty()) {
            fact6.setResult("OK");
        } else {
            fact6.setResult("Ошибка");
            flkP.getPrList().add(new FlkErr(fact6.getTest(), fact6.getDescription()));
        }
        CheckTabController.checkFactList.add(fact6);

        q015ValidationService.validate(zlList, persList, flkP);

        return flkP;
    }

}
