package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class V012Service extends AbstractNsiService {

    private V012Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V012Packet.class, "nsi/V012.ZIP");
    }

    public boolean isValidIshodOnUslOkAndDate(Integer ishod, Integer uslOk, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .filter(o -> Objects.equals(uslOk, o.getDlUslov()))
                .anyMatch(o -> o.getIdiz().equals(ishod));
    }
}
