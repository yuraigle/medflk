package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V014Service extends AbstractNsiService {

    private V014Packet packet;

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
