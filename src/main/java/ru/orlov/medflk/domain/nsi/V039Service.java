package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V039Service extends AbstractNsiService {

    private V039Packet packet;

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
        return "Классификатор видов занятости";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V039Packet.class, "nsi/V039.ZIP");
    }

    public boolean isValidIdVzOnDate(Integer idVz, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdVz().equals(idVz));
    }
}
