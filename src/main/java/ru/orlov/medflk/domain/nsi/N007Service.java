package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N007Service extends AbstractNsiService {

    private N007Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(N007Packet.class, "nsi/N007.ZIP");
    }

    public boolean isValidCodeOnDate(Integer code, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdMrf().equals(code));
    }
}
