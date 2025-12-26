package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N008Service extends AbstractNsiService {

    private N008Packet packet;

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
        return "Классификатор результатов гистологических исследований";
    }

    @Override
    public void initPacket() {
        packet = readNsi(N008Packet.class, "nsi/N008.ZIP");
    }

    public boolean isValidResOnCodeAndDate(Integer res, Integer code, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdRm().equals(res) && o.getIdMrf().equals(code));
    }
}
