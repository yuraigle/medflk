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
public class V001Service extends AbstractNsiService {

    private List<V001Packet.V001.Data> v001List;

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
        return "Номенклатура медицинских услуг";
    }

    private V001Packet readNsi() {
        String filename = "nsi/V001.ZIP";

        try (
                ZipFile zf = new ZipFile(filename)
        ) {
            ZipEntry entry = zf.entries().nextElement();
            date = entry.getLastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            version = StringUtils.substringBetween(entry.getName(), "_", ".xml");
            Unmarshaller unmarshaller = JAXBContext.newInstance(V001Packet.class).createUnmarshaller();
            return (V001Packet) unmarshaller.unmarshal(zf.getInputStream(entry));
        } catch (IOException | JAXBException | ClassCastException e) {
            log.error("Ошибка чтения XML справочника {}: {}", filename, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initPacket() {
        V001Packet packet = readNsi();

        v001List = packet.getEntries().getEntryList().stream()
                .map(V001Packet.V001::getData)
                .toList();
    }

    public Boolean isValidUslCode(String code) {
        return v001List.stream()
                .anyMatch(o -> o.getSCode().equals(code));
    }

}
