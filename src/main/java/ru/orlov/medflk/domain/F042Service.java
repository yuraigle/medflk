package ru.orlov.medflk.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class F042Service extends AbstractNsiService {

    private F042Packet packet;

    @Value("${regional.okato}")
    private String regionalOkato;

    @Override
    public void initPacket() {
        packet = readNsi(F042Packet.class, "nsi/F042.ZIP");
    }

    public boolean isValidRegionalExpertOnDate(String expert, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> o.getOktmoP().startsWith(regionalOkato))
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getUidekmp().equals(expert));
    }

}
