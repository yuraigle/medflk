package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N005Service extends AbstractNsiService {

    private N005Packet packet;

    @Override
    public String getVersion() {
        return packet == null ? null : packet.getZglv().getVersion();
    }

    @Override
    public LocalDate getDate() {
        return packet == null ? null : packet.getZglv().getDate();
    }

    @Override
    public String getDescription() {
        return "Классификатор Metastasis";
    }

    @Override
    public void initPacket() {
        packet = readNsi(N005Packet.class, "nsi/N005.ZIP");
    }

    public boolean isValidMetastasisOnDsAndDate(Integer metastasis, String ds, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .filter(o -> o.getDsM().equals(ds))
                .anyMatch(o -> o.getIdM().equals(metastasis));
    }
}
