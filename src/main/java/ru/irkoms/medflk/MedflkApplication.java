package ru.irkoms.medflk;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.irkoms.medflk.domain.AbstractNsiService;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;
import ru.irkoms.medflk.service.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
@SpringBootApplication
public class MedflkApplication implements CommandLineRunner {

    private final ApplicationContext ctx;
    private final NsiDownloaderService nsiDownloaderService;
    private final RegistryReaderService registryReaderService;
    private final SchemaValidationService schemaValidationService;
    private final Q015ValidationService q015ValidationService;
    private final DepersonalizeService depersonalizeService;

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

        if (args[0].equals("make-demo")) {
            String dir = args[1];
            depersonalizeService.depersonalizeDir(dir);
            System.exit(0);
        }

        initNsiPackets();

        try (Stream<Path> files = Files.walk(Path.of(args[0]))) {
            files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toFile().getName().startsWith("H"))
                    .forEach(p -> checkFile(p.toFile()));
        } catch (Exception e) {
            log.error(e);
        }

        System.exit(0);
    }

    private void checkFile(File zip) {
        if (!zip.exists()) {
            log.error("Файл {} не найден", zip.getName());
            return;
        }

        log.info("Обрабатывается файл {} ({}Kb)", zip.getName(), zip.length() / 1024);

        ZlList zlList = null;
        PersList persList = null;

        try {
            zlList = registryReaderService.parseZlList(zip);
            persList = registryReaderService.parsePersList(zip);
        } catch (Exception e) {
            log.error(e);
        }

        if (zlList == null || persList == null) {
            log.error("Ошибка чтения файла {}", zip.getName());
            return;
        }

        List<FlkP.Pr> schemaErrors = schemaValidationService.validate(zlList);
        schemaErrors.addAll(schemaValidationService.validate(persList));

        if (!schemaErrors.isEmpty()) {
            for (FlkP.Pr error : schemaErrors) {
                log.error("{}", error);
            }
            return;
        }

        List<FlkP.Pr> q015Errors = q015ValidationService.validate(zlList, persList);
        if (!q015Errors.isEmpty()) {
            log.error("{} ошибок Q015", q015Errors.size());
            return;
        }

        log.info("OK");
    }

    private void initNsiPackets() {
        Arrays.stream(ctx.getBeanDefinitionNames())
                .filter(name -> name.matches("^.*[a-zA-Z][0-9]{3}Service$"))
                .forEach(name -> {
                    Object bean = ctx.getBean(name);
                    if (bean instanceof AbstractNsiService) {
                        ((AbstractNsiService) bean).initPacket();
                    }
                });
    }
}
