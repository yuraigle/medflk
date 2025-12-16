package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V024Service extends AbstractNsiService {

    private V024Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V024Packet.class, "nsi/V024.ZIP");
    }

    public boolean isValidCritOnDate(String crit, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIddkk().equals(crit));
    }
}
