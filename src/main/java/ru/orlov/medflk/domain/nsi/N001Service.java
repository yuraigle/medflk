package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N001Service extends AbstractNsiService {

    private N001Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(N001Packet.class, "nsi/N001.ZIP");
    }

    public boolean isValidProtOnDate(Integer prot, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdProt().equals(prot));
    }
}
