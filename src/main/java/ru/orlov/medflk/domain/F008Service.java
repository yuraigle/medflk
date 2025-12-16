package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class F008Service extends AbstractNsiService {

    private F008Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(F008Packet.class, "nsi/F008.ZIP");
    }

    public boolean isValidVpolisOnDate(Integer vpolis, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIddoc().equals(vpolis));
    }

}
