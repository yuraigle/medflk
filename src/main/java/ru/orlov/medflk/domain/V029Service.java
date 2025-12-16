package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V029Service extends AbstractNsiService {

    private V029Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V029Packet.class, "nsi/V029.ZIP");
    }

    public boolean isValidIdMetOnDate(Integer idMet, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdmet().equals(idMet));
    }
}
