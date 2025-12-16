package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V019Service extends AbstractNsiService {

    private V019Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V019Packet.class, "nsi/V019.ZIP");
    }

    public boolean isValidMetOnDate(Integer met, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdhm().equals(met));
    }

}
