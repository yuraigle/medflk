package ru.irkoms.medflk.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.irkoms.medflk.domain.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

/**
 * Не используем никакую БД, просто загружаем все справочники из XML в память
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class NsiReaderService {

    private final Q015Service q015Service;
    private final F011Service f011Service;
    private final F032Service f032Service;
    private final V006Service v006Service;
    private final V008Service v008Service;
    private final V009Service v009Service;
    private final V014Service v014Service;

    public void readAll() {
        long startedAt = System.currentTimeMillis();

        try {
            q015Service.setPacket(readNsi(Q015Packet.class, "nsi/Q015.ZIP"));
            q015Service.attachCheckerBeans();

            f011Service.setPacket(readNsi(F011Packet.class, "nsi/F011.ZIP"));
            f032Service.setPacket(readNsi(F032Packet.class, "nsi/F032.ZIP"));
            v006Service.setPacket(readNsi(V006Packet.class, "nsi/V006.ZIP"));
            v008Service.setPacket(readNsi(V008Packet.class, "nsi/V008.ZIP"));
            v009Service.setPacket(readNsi(V009Packet.class, "nsi/V009.ZIP"));
            v014Service.setPacket(readNsi(V014Packet.class, "nsi/V014.ZIP"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("NSI loaded in {} ms", System.currentTimeMillis() - startedAt);
    }

    private static <T> T readNsi(Class<T> cls, String filename) throws Exception {
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
