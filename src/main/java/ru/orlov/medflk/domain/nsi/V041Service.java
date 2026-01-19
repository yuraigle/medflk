package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V041Service extends AbstractNsiService {

    private V041Packet packet;

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
        return "Классификатор коэффициентов сложности лечения пациента";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V041Packet.class, "nsi/V041.ZIP");
    }

    public boolean isValidIdMopOnDate(String idSl, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdSl().equals(idSl));
    }
}
