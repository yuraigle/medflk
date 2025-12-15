package ru.irkoms.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V016Service extends AbstractNsiService {

    private V016Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V016Packet.class, "nsi/V016.ZIP");
    }

    public boolean isValidDispTypeOnDate(String dispType, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIddt().equals(dispType));
    }

}
