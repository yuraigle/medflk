package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N015Service extends AbstractNsiService {

    private N015Packet packet;

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
        return "Классификатор линий лекарственной терапии";
    }

    @Override
    public void initPacket() {
        packet = readNsi(N015Packet.class, "nsi/N015.ZIP");
    }

    public boolean isValidTipOnDate(Integer tip, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdTLekL().equals(tip));
    }
}
