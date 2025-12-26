package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V023Service extends AbstractNsiService {

    private V023Packet packet;

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
        return "Классификатор клинико-статистических групп";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V023Packet.class, "nsi/V023.ZIP");
    }

    public boolean isValidNKsgOnDate(String nKsg, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getNKsg().equals(nKsg));
    }

    public V023Packet.V023 getKsgOnDate(String nKsg, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .filter(o -> o.getNKsg().equals(nKsg))
                .findFirst().orElse(null);
    }
}
