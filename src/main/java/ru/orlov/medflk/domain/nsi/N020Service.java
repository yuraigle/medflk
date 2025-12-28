package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N020Service extends AbstractNsiService {

    private N020Packet packet;

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
        return "Классификатор лекарственных препаратов, применяемых при проведении лекарственной терапии";
    }

    @Override
    public void initPacket() {
        packet = readNsi(N020Packet.class, "nsi/N020.ZIP");
    }

    public boolean isValidRegnumOnDate(String regnum, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdLekp().equals(regnum));
    }
}
