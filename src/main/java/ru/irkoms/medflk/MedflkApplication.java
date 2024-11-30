package ru.irkoms.medflk;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;
import ru.irkoms.medflk.service.NsiReaderService;
import ru.irkoms.medflk.service.Q015ValidationService;
import ru.irkoms.medflk.service.RegistryReaderService;
import ru.irkoms.medflk.service.SchemaValidationService;

import java.io.File;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@SpringBootApplication
public class MedflkApplication implements CommandLineRunner {

    private final NsiReaderService nsiReaderService;
    private final RegistryReaderService registryReaderService;
    private final SchemaValidationService schemaValidationService;
    private final Q015ValidationService q015ValidationService;

    public static void main(String[] args) {
        SpringApplication.run(MedflkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            log.info("Usage: java -jar medflk.jar filename.zip");
            return;
        }

        String filename = args[0];
        log.info("Processing file: {}", filename);

        File zip = new File(filename);
        if (!zip.exists()) {
            log.error("File not found: {}", filename);
            return;
        }

        nsiReaderService.readNsi();

        AZlList zlList = null;
        APersList persList = null;

        try {
            zlList = registryReaderService.parseZlList(zip);
            persList = registryReaderService.parsePersList(zip);
        } catch (Exception e) {
            log.error(e);
        }

        if (zlList == null || persList == null) {
            log.error("Error while reading file {}", filename);
            System.exit(1);
        }

        List<FlkP.Pr> schemaErrors = schemaValidationService.validate(zlList);
        schemaErrors.addAll(schemaValidationService.validate(persList));

        if (!schemaErrors.isEmpty()) {
            log.error("Schema validation errors:");
            for (FlkP.Pr error : schemaErrors) {
                log.error("{}", error);
            }
            System.exit(1);
        }

        List<FlkP.Pr> q015Errors = q015ValidationService.validate(zlList, persList);
        if (!q015Errors.isEmpty()) {
            log.error("Q015 validation errors:");
            for (FlkP.Pr error : q015Errors) {
                log.error("{}", error);
            }
            System.exit(1);
        }

        log.info("OK");
        System.exit(0);
    }
}
