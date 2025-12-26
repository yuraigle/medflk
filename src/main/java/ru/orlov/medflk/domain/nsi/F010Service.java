package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class F010Service extends AbstractNsiService {

    private F010Packet packet;

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
        return "Классификатор субъектов Российской Федерации";
    }

    @Override
    public void initPacket() {
        packet = readNsi(F010Packet.class, "nsi/F010.ZIP");
    }

    public boolean isValidRegionOkatoOnDate(String okato, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getKodOkato().equals(okato));
    }

}
