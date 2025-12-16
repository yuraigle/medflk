package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V020Service extends AbstractNsiService {

    private V020Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V020Packet.class, "nsi/V020.ZIP");
    }

    public boolean isValidProfilKOnDate(Integer profilK, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdkPr().equals(profilK));
    }
}
