package ru.orlov.medflk.domain.nsi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class M002Service extends AbstractNsiService {

    private List<M002Packet.M002> m002List;

    @Value("${regional.tnm_version}")
    private Integer tnmVersion;

    @Override
    public void initPacket() {
        M002Packet packet = readNsi(M002Packet.class, "nsi/M002.ZIP");

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
