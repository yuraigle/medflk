package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V026Service extends AbstractNsiService {

    private V026Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V026Packet.class, "nsi/V026.ZIP");
    }

    public boolean isValidNKpgOnDate(String nKpg, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getKKpg().equals(nKpg));
    }
}
