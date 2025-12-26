package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V021Service extends AbstractNsiService {

    private V021Packet packet;

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
        return "Классификатор медицинских специальностей";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V021Packet.class, "nsi/V021.ZIP");
    }

    public boolean isValidPrvsOnDate(Integer prvs, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdspec().equals(prvs));
    }
}
