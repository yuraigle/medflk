package ru.irkoms.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class V009Service extends AbstractNsiService {

    private V009Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(V009Packet.class, "nsi/V009.ZIP");
    }

    public boolean isValidResultOnUslOkAndDate(Integer rslt, Integer uslOk, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .filter(o -> Objects.equals(uslOk, o.getDlUslov()))
                .anyMatch(o -> o.getIdrmp().equals(rslt));
    }
}
