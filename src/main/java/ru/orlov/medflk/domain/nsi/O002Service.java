package ru.orlov.medflk.domain.nsi;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class O002Service extends AbstractNsiService {

    private final Map<String, HashSet<String>> okatoMap = new HashMap<>(); // 5 => 6

    @Override
    public void initPacket() {
        O002Packet packet = readNsi(O002Packet.class, "nsi/O002.ZIP");

        for (O002Packet.O002 z : packet.getZapList()) {
            String k = z.getTer() + z.getKod1();
            String v = z.getKod2() + z.getKod3();
            okatoMap.computeIfAbsent(k, s -> new HashSet<>()).add(v);
        }
    }

    public boolean isValidOkato(String okato) {
        if (okato == null || okato.length() != 11) {
            return false;
        }

        String s1 = okato.substring(0, 5);
        String s2 = okato.substring(5, 11);

        return okatoMap.containsKey(s1) && okatoMap.get(s1).contains(s2);
    }

}
