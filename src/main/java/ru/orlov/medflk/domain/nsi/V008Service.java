package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V008Service extends AbstractNsiService {

    private V008Packet packet;

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
        return "Классификатор видов медицинской помощи";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V008Packet.class, "nsi/V008.ZIP");
    }

    public boolean isValidVidPomOnDate(Integer vidPom, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdvmp().equals(vidPom));
    }

}
