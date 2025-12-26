package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V018Service extends AbstractNsiService {

    private V018Packet packet;

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
        return "Классификатор видов высокотехнологичной медицинской помощи";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V018Packet.class, "nsi/V018.ZIP");
    }

    public boolean isValidHvidOnDate(String vid, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdhvid().equals(vid));
    }

}
