package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V042Service extends AbstractNsiService {

    private V042Packet packet;

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
        return "Классификатор причин оплаты за прерванный случай лечения";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V042Packet.class, "nsi/V042.ZIP");
    }

    public boolean isValidIdPrOnDate(Integer idPr, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdPr().equals(idPr));
    }
}
