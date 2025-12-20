package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N011Service extends AbstractNsiService {

    private N011Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(N011Packet.class, "nsi/N011.ZIP");
    }

    public boolean isValidResOnCodeAndDate(Integer res, Integer code, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdRi().equals(res) && o.getIdIgh().equals(code));
    }
}
