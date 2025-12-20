package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N002Service extends AbstractNsiService {

    private N002Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(N002Packet.class, "nsi/N002.ZIP");
    }

    public boolean isValidStadOnDsAndDate(Integer stad, String ds, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .filter(o -> o.getDsSt().equals(ds))
                .anyMatch(o -> o.getIdSt().equals(stad));
    }
}
