package ru.orlov.medflk.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class F006Service extends AbstractNsiService {

    private F006Packet packet;

    @Override
    public void initPacket() {
        packet = readNsi(F006Packet.class, "nsi/F006.ZIP");
    }

    public boolean isValidSTipOnDate(Integer sTip, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdvid().equals(sTip));
    }

}
