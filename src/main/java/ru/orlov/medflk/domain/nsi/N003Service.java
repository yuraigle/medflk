package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N003Service extends AbstractNsiService {

    private N003Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(N003Packet.class, "nsi/N003.ZIP");
    }

    public boolean isValidTumorOnDsAndDate(Integer tumor, String ds, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .filter(o -> o.getDsT().equals(ds))
                .anyMatch(o -> o.getIdT().equals(tumor));
    }
}
