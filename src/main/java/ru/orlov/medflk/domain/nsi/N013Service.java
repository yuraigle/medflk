package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N013Service extends AbstractNsiService {

    private N013Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(N013Packet.class, "nsi/N013.ZIP");
    }

    public boolean isValidTipOnDate(Integer tip, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdTlech().equals(tip));
    }
}
