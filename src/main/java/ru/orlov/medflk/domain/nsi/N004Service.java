package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N004Service extends AbstractNsiService {

    private N004Packet packet;

    @Override
    public String getVersion() {
        return packet == null ? null : packet.getZglv().getVersion();
    }

    @Override
    public LocalDate getDate() {
        return packet == null ? null : packet.getZglv().getDate();
    }

    @Override
    public String getDescription() {
        return "Классификатор Nodus";
    }

    @Override
    public void initPacket() {
        packet = readNsi(N004Packet.class, "nsi/N004.ZIP");
    }

    public boolean isValidNodusOnDsAndDate(Integer nodus, String ds, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .filter(o -> o.getDsN().equals(ds))
                .anyMatch(o -> o.getIdN().equals(nodus));
    }
}
