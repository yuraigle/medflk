package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N017Service extends AbstractNsiService {

    private N017Packet packet;

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
        return "Классификатор типов лучевой терапии";
    }

    @Override
    public void initPacket() {
        packet = readNsi(N017Packet.class, "nsi/N017.ZIP");
    }

    public boolean isValidTipOnDate(Integer tip, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdTLuch().equals(tip));
    }
}
