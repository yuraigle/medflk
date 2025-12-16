package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class F014Service extends AbstractNsiService {

    private F014Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(F014Packet.class, "nsi/F014.ZIP");
    }

    public boolean isValidSOsnOnDate(Integer sOsn, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getKod().equals(sOsn));
    }

}
