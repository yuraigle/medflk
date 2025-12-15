package ru.irkoms.medflk.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.irkoms.medflk.jaxb.PersList;
import ru.irkoms.medflk.jaxb.ZlList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Log4j2
@Service
public class RegistryReaderService {

    public ZlList parseZlList(File zip) throws Exception {
        try {
            return parseList(zip, ZlList.class);
        } catch (Exception e) {
            throw new Exception("Ошибка чтения XML/ZIP");
        }
    }

    public PersList parsePersList(File zip) throws Exception {
        try {
            return parseList(zip, PersList.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new Exception("Ошибка чтения XML/ZIP");
        }
    }

    private <T> T parseList(File zip, Class<T> cls) throws Exception {
        try (ZipFile zf = new ZipFile(zip)) {
            Unmarshaller unmarshaller = JAXBContext.newInstance(cls).createUnmarshaller();
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName().toUpperCase();
                String clsName = cls.getSimpleName();

                try (InputStream is = zf.getInputStream(entry)) {
                    if (clsName.startsWith("ZlList") && name.matches("^[CHTD].*\\.XML$")) {
                        return cls.cast(unmarshaller.unmarshal(is));
                    } else if (clsName.startsWith("PersList") && name.matches("^L.*\\.XML$")) {
                        return cls.cast(unmarshaller.unmarshal(is));
                    }
                }
            }
        } catch (IOException | JAXBException | ClassCastException e) {
            log.error("Ошибка чтения XML/ZIP: {}", e.getMessage());
            throw e;
        }

        throw new Exception();
    }

}
