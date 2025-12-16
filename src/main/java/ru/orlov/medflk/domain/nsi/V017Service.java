package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V017Service extends AbstractNsiService {

    private V017Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V017Packet.class, "nsi/V017.ZIP");
    }

    public boolean isValidRsltDOnDate(Integer rsltD, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIddr().equals(rsltD));
    }

}
