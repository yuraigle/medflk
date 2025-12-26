package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V014Service extends AbstractNsiService {

    private V014Packet packet;

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
        return "Классификатор форм оказания медицинской помощи";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V014Packet.class, "nsi/V014.ZIP");
    }

    public boolean isValidForPomOnDate(Integer forPom, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdfrmmp().equals(forPom));
    }

}
