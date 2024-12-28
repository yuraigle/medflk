package ru.irkoms.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class F011Service extends AbstractNsiService {

    private F011Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(F011Packet.class, "nsi/F011.ZIP");
    }

    public boolean isValidIdDocOnDate(Integer idDoc, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdDoc().equals(idDoc));
    }

}
