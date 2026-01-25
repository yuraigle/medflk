package ru.orlov.medflk.task;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.FlkP;
import ru.orlov.medflk.service.FileValidatorService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static ru.orlov.medflk.Utils.*;

@Service
@RequiredArgsConstructor
public class DirValidatorTask {

    @Value("${app.dir_ok}")
    private String dirOk;

    @Value("${app.dir_flk}")
    private String dirFlk;

    public static final List<File> queue = new ArrayList<>();
    private final FileValidatorService validator;

    public Task<Void> getTaskWithStatus(StringProperty status) {
        return new Task<>() {

            @Override
            protected Void call() throws Exception {
                while (true) {
                    if (!queue.isEmpty()) {
                        File file = queue.getFirst();
                        process(file);
                        queue.remove(file);
                    } else {
                        Thread.sleep(500);
                    }
                }
            }

            private void process(File file) throws Exception {
                waitForFileUnlock(file, 30000);

                FlkP flkP = validator.validate(file, false);

                int cntErr = flkP.getPrList() == null ? 0 : flkP.getPrList().size();
                if (cntErr == 0) {
                    Path pathOk = Paths.get(dirOk, file.getName());
                    Files.move(file.toPath(), pathOk, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Path pathFlk = Paths.get(dirFlk, file.getName());
                    Files.move(file.toPath(), pathFlk, StandardCopyOption.REPLACE_EXISTING);
                    generateProtocol(flkP);
                }

                appendLine(String.format("Проверен файл %s. %s",
                        file.getName(), cntErr == 0 ? "OK" : pluralErr(cntErr)));
            }

            private void generateProtocol(FlkP flkP) {
                File outZip = Paths.get(dirFlk, flkP.getFname() + ".ZIP").toFile();

                try (
                        FileOutputStream fos = new FileOutputStream(outZip);
                        ZipOutputStream zos = new ZipOutputStream(fos)
                ) {
                    ZipEntry ze = new ZipEntry(flkP.getFname() + ".XML");
                    zos.putNextEntry(ze);

                    JAXBContext jaxbContext = JAXBContext.newInstance(FlkP.class);
                    Marshaller marshaller = jaxbContext.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    marshaller.setProperty(Marshaller.JAXB_ENCODING, "windows-1251");
                    marshaller.marshal(flkP, zos);
                } catch (IOException | JAXBException e) {
                    System.err.println(e.getMessage());
                }
            }

            private void appendLine(String line) {
                String dt = LocalDateTime.now().format(fmtDtRus);

                String ss = status.get() == null ? "" : status.get();
                String s = ss + dt + "  " + line + "\n";
                status.set(s);
            }
        };
    }
}
