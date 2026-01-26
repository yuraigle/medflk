package ru.orlov.medflk.task;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static ru.orlov.medflk.Utils.pluralErr;
import static ru.orlov.medflk.Utils.waitForFileUnlock;

@Service
@RequiredArgsConstructor
public class DirValidatorTask {

    private final FileValidatorService validator;

    public Task<Void> getTask(File dirOk, File dirFlk) {
        return new Task<>() {

            @Override
            protected Void call() throws Exception {
                while (true) {
                    if (!DirMonitoringTask.queue.isEmpty()) {
                        File file = DirMonitoringTask.queue.getFirst();
                        process(file);
                        DirMonitoringTask.queue.remove(file);
                    } else {
                        Thread.sleep(500);
                    }
                }
            }

            private void process(File file) throws Exception {
                long startedAt = System.currentTimeMillis();
                waitForFileUnlock(file, 30000);

                long fileSize = file.length() / 1024;
                FlkP flkP = validator.validate(file, false);

                int cntErr = flkP.getPrList() == null ? 0 : flkP.getPrList().size();
                File dir = cntErr == 0 ? dirOk : dirFlk;
                Path out = Paths.get(dir.getAbsolutePath(), file.getName());
                Files.move(file.toPath(), out, StandardCopyOption.REPLACE_EXISTING);
                if (cntErr > 0) {
                    generateProtocol(flkP);
                }

                String statusLine = String.format("Файл %s (%sКб) проверен за %sс. %s",
                        file.getName(), fileSize,
                        (System.currentTimeMillis() - startedAt) / 1000,
                        cntErr == 0 ? "OK" : pluralErr(cntErr));
                updateMessage(statusLine);
            }

            private void generateProtocol(FlkP flkP) {
                File outZip = Paths.get(dirFlk.getAbsolutePath(), flkP.getFname() + ".ZIP").toFile();

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
        };
    }
}
