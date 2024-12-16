package ru.irkoms.medflk.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class NsiReaderService {

    /**
     * Ожидаем 1 xml файл справочника внутри zip
     */
    public static <T> T readNsi(Class<T> cls, String filename) throws Exception {
        try (
                ZipFile zf = new ZipFile(filename);
                InputStream is = zf.getInputStream(zf.entries().nextElement());
        ) {
            Unmarshaller unmarshaller = JAXBContext.newInstance(cls).createUnmarshaller();
            return cls.cast(unmarshaller.unmarshal(is));
        } catch (IOException e) {
            log.error("Error while reading ZIP: {}", e.getMessage());
            throw e;
        } catch (JAXBException | ClassCastException e) {
            log.error("Error while reading XML: {}", e.getMessage());
            throw e;
        }
    }

}
