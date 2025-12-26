package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V002Service extends AbstractNsiService {

    private V002Packet packet;

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
        return "Классификатор профилей оказанной медицинской помощи";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V002Packet.class, "nsi/V002.ZIP");
    }

    public boolean isValidProfilOnDate(Integer profil, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdpr().equals(profil));
    }
}
