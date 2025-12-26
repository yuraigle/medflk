package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.zip.ZipFile;

/**
 * Не используем никакую БД, просто загружаем все справочники из XML в память
 */

@Log4j2
public abstract class AbstractNsiService {

    public static <T> T readNsi(Class<T> cls, String filename) {
        try (
                ZipFile zf = new ZipFile(filename);
                InputStream is = zf.getInputStream(zf.entries().nextElement())
        ) {
            Unmarshaller unmarshaller = JAXBContext.newInstance(cls).createUnmarshaller();
            return cls.cast(unmarshaller.unmarshal(is));
        } catch (IOException | JAXBException | ClassCastException e) {
            log.error("Ошибка чтения XML справочника {}: {}", filename, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public abstract void initPacket();

    public String getVersion() {
        return "";
    }

    public LocalDate getDate() {
        return LocalDate.now();
    }

    public String getDescription() {
        return "";
    }

}
