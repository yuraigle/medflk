package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Log4j2
@Service
public class M002Service extends AbstractNsiService {

    private List<M002Packet.M002> m002List;

    @Value("${regional.tnm_version}")
    private Integer tnmVersion;

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
        return "Топография для классификации TNM";
    }

    private M002Packet readNsi(String filename) {
        try (
                ZipFile zf = new ZipFile(filename);
        ) {
            ZipEntry entry = zf.entries().nextElement();
            date = entry.getLastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            version = StringUtils.substringBetween(entry.getName(), "_", ".xml");
            Unmarshaller unmarshaller = JAXBContext.newInstance(M002Packet.class).createUnmarshaller();
            return (M002Packet) unmarshaller.unmarshal(zf.getInputStream(entry));
        } catch (IOException | JAXBException | ClassCastException e) {
            log.error("Ошибка чтения XML справочника {}: {}", filename, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initPacket() {
        M002Packet packet = readNsi("nsi/M002.ZIP");

        if (tnmVersion == 7) {
            m002List = packet.getEntries().getEntryList().stream()
                    .filter(o -> o.getTnm8().equalsIgnoreCase("true"))
                    .toList();
        } else if (tnmVersion == 8) {
            m002List = packet.getEntries().getEntryList().stream()
                    .filter(o -> o.getTnm8().equalsIgnoreCase("true"))
                    .toList();
        } else {
            m002List = packet.getEntries().getEntryList().stream()
                    .toList();
        }
    }

    public String findMkbOTopography(String ds) {
        return m002List.stream()
                .filter(o -> o.getIcd10().equals(ds))
                .map(M002Packet.M002::getIcdo)
                .findFirst().orElse(null);
    }

}
