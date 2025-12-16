package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V028Service extends AbstractNsiService {

    private V028Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V028Packet.class, "nsi/V028.ZIP");
    }

    public boolean isValidIdvnOnDate(Integer idvn, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdvn().equals(idvn));
    }
}
