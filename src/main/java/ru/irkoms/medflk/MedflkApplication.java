package ru.irkoms.medflk;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.irkoms.medflk.domain.AbstractNsiService;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;
import ru.irkoms.medflk.service.NsiDownloaderService;
import ru.irkoms.medflk.service.Q015ValidationService;
import ru.irkoms.medflk.service.RegistryReaderService;
import ru.irkoms.medflk.service.SchemaValidationService;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@SpringBootApplication
public class MedflkApplication implements CommandLineRunner {

    private final ApplicationContext ctx;
    private final NsiDownloaderService nsiDownloaderService;
    private final RegistryReaderService registryReaderService;
    private final SchemaValidationService schemaValidationService;
    private final Q015ValidationService q015ValidationService;

    public static void main(String[] args) {
        SpringApplication.run(MedflkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            log.info("Usage:");
            log.info("java -jar medflk.jar filename.zip to check file");
            log.info("java -jar medflk.jar update-nsi to update nsi");

            System.exit(0);
        }

        if (args[0].equals("update-nsi")) {
            nsiDownloaderService.updateAll();
            System.exit(0);
        }

        File zip = new File(args[0]);
        if (!zip.exists()) {
            log.error("File not found: {}", zip.getName());
            return;
        }

        initNsiPackets();
        log.info("Processing file: {} ({}Kb)", zip.getName(), zip.length() / 1024);

        AZlList zlList = null;
        APersList persList = null;
        try {
            zlList = registryReaderService.parseZlList(zip);
            persList = registryReaderService.parsePersList(zip);
        } catch (Exception e) {
            log.error(e);
        }

        if (zlList == null || persList == null) {
            log.error("Error while reading file {}", zip.getName());
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

    private void initNsiPackets() {
        long start = System.currentTimeMillis();

        Arrays.stream(ctx.getBeanDefinitionNames())
                .filter(name -> name.matches("^.*[a-zA-Z][0-9]{3}Service$"))
                .forEach(name -> {
                    Object bean = ctx.getBean(name);
                    if (bean instanceof AbstractNsiService) {
                        ((AbstractNsiService) bean).initPacket();
                    }
                });

        log.info("Nsi packets initialized in {} ms", System.currentTimeMillis() - start);
    }
}
