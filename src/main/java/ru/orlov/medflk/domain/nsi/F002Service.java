package ru.orlov.medflk.domain.nsi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class F002Service extends AbstractNsiService {

    private final Set<String> regionalCodes = new HashSet<>();

    private F002Packet packet;

    @Value("${regional.okato}")
    private String regionalOkato;

    @Override
    public void initPacket() {
        packet = readNsi(F002Packet.class, "nsi/F002.ZIP");

        // ускоряем поиск по справочнику
        if (packet != null) {
            packet.getZapList().stream()
                    .filter(o -> o.getTfOkato().startsWith(regionalOkato))
                    .forEach(o -> regionalCodes.add(o.getSmocod()));
        }
    }

    public boolean isValidSmoOnDate(String code, LocalDate d1) {
        return packet.getZapList().stream()
                .anyMatch(o -> o.getSmocod().equals(code));
    }

    public boolean isValidSmoOgrnOnDate(String smo, String ogrn, LocalDate d1) {
        if (smo == null) { // нет кода СМО - проверяем только ОГРН
            return packet.getZapList().stream()
                    .anyMatch(o -> o.getOgrn().equals(ogrn));
        }

        // если указан код СМО и ОГРН, проверяем в связке
        return packet.getZapList().stream()
                .anyMatch(o -> o.getSmocod().equals(smo) && o.getOgrn().equals(ogrn));
    }

    public boolean isValidRegionalSmoOnDate(String codeMo, LocalDate d1) {
        return regionalCodes.contains(codeMo);
    }
}
