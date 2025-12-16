package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V006Service extends AbstractNsiService {

    private V006Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V006Packet.class, "nsi/V006.ZIP");
    }

    public boolean isValidUslOkOnDate(Integer uslOk, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdump().equals(uslOk));
    }

}
