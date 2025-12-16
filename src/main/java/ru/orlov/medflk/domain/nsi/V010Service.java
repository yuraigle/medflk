package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V010Service extends AbstractNsiService {

    private V010Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V010Packet.class, "nsi/V010.ZIP");
    }

    public boolean isValidIdspOnDate(Integer idsp, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdsp().equals(idsp));
    }
}
