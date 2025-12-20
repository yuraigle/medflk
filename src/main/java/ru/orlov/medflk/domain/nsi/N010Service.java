package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N010Service extends AbstractNsiService {

    private N010Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(N010Packet.class, "nsi/N010.ZIP");
    }

    public boolean isValidCodeOnDate(Integer code, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdIgh().equals(code));
    }
}
