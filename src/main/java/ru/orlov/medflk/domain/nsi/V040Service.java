package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V040Service extends AbstractNsiService {

    private V040Packet packet;

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
        return "Классификатор мест обращений (посещений)";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V040Packet.class, "nsi/V040.ZIP");
    }

    public boolean isValidIdMopOnDate(Integer idMop, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdMop().equals(idMop));
    }
}
