package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class V020Service extends AbstractNsiService {

    private V020Packet packet;

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
        return "Классификатор профиля койки";
    }

    @Override
    public void initPacket() {
        packet = readNsi(V020Packet.class, "nsi/V020.ZIP");
    }

    public boolean isValidProfilKOnDate(Integer profilK, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdkPr().equals(profilK));
    }
}
