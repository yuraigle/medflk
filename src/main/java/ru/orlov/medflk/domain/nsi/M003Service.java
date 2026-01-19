package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Log4j2
@Service
public class M003Service extends AbstractNsiService {

    private List<M003Packet.M003> m003List;

    private String version;
    private LocalDate date;

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return "Профили медицинской помощи";
    }

    private M003Packet readNsi() {
        String filename = "nsi/M003.ZIP";

        try (
                ZipFile zf = new ZipFile(filename)
        ) {
            ZipEntry entry = zf.entries().nextElement();
            date = entry.getLastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            version = StringUtils.substringBetween(entry.getName(), "_", ".xml");
            Unmarshaller unmarshaller = JAXBContext.newInstance(M003Packet.class).createUnmarshaller();
            return (M003Packet) unmarshaller.unmarshal(zf.getInputStream(entry));
        } catch (IOException | JAXBException | ClassCastException e) {
            log.error("Ошибка чтения XML справочника {}: {}", filename, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initPacket() {
        M003Packet packet = readNsi();
        m003List = packet.getEntries().getEntryList().stream().toList();
    }

    public Boolean isValidProfil(Integer profil) {
        return m003List.stream()
                .anyMatch(o -> o.getId().equals(profil));
    }

}
