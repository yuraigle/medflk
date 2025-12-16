package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V027Service extends AbstractNsiService {

    private V027Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V027Packet.class, "nsi/V027.ZIP");
    }

    public boolean isValidCZabOnDate(Integer cZab, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdcz().equals(cZab));
    }
}
