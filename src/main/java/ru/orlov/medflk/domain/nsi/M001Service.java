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
public class M001Service extends AbstractNsiService {

    private List<M001Packet.M001> m001List;

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
        return " Международная классификация болезней МКБ-10";
    }

    private M001Packet readNsi() {
        String filename = "nsi/M001.ZIP";

        try (
                ZipFile zf = new ZipFile(filename)
        ) {
            ZipEntry entry = zf.entries().nextElement();
            date = entry.getLastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            version = StringUtils.substringBetween(entry.getName(), "_", ".xml");
            Unmarshaller unmarshaller = JAXBContext.newInstance(M001Packet.class).createUnmarshaller();
            return (M001Packet) unmarshaller.unmarshal(zf.getInputStream(entry));
        } catch (IOException | JAXBException | ClassCastException e) {
            log.error("Ошибка чтения XML справочника {}: {}", filename, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initPacket() {
        M001Packet packet = readNsi();

        m001List = packet.getEntries().getEntryList().stream().toList();
    }

    public Boolean isValidDs(String ds) {
        return m001List.stream()
                .filter(o -> o.getActual() == 1)
                .anyMatch(o -> o.getMkbCode().equals(ds));
    }

}
