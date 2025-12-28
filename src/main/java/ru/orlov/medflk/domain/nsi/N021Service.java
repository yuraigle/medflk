package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class N021Service extends AbstractNsiService {

    private N021Packet packet;

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
        return "Классификатор соответствия лекарственного препарата схеме лекарственной терапии";
    }

    @Override
    public void initPacket() {
        packet = readNsi(N021Packet.class, "nsi/N021.ZIP");
    }

    public boolean isValidRegnumDopOnCodeShAndDate(String regnumDop, String codeSh, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getCodeSh().equals(codeSh) && o.getIdLekpExt().equals(regnumDop));
    }
}
