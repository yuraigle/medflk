package ru.orlov.medflk.domain.nsi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class F032Service extends AbstractNsiService {

    private final Set<String> regionalCodes = new HashSet<>();

    private F032Packet packet;

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
        return "Реестр медицинских организаций, осуществляющих деятельность в сфере обязательного медицинского страхования";
    }

    @Value("${regional.okato}")
    private String regionalOkato;

    @Override
    public void initPacket() {
        packet = readNsi(F032Packet.class, "nsi/F032.ZIP");

        // ускоряем поиск по справочнику
        if (packet != null) {
            packet.getZapList().stream()
                    .filter(o -> o.getOktmoP().startsWith(regionalOkato))
                    .forEach(o -> regionalCodes.add(o.getMcod()));
        }
    }

    public boolean isValidCodeMoOnDate(String codeMo, LocalDate d1) {
        return packet.getZapList().stream()
                .anyMatch(o -> o.getMcod().equals(codeMo));
    }

    public boolean isValidRegionalCodeMoOnDate(String codeMo, LocalDate d1) {
        return regionalCodes.contains(codeMo);
    }
}
