package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V025Service extends AbstractNsiService {

    private V025Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V025Packet.class, "nsi/V025.ZIP");
    }

    public boolean isValidPCelOnDate(String pCel, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdpc().equals(pCel));
    }
}
