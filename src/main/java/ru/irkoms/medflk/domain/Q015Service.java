package ru.irkoms.medflk.domain;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.irkoms.medflk.service.NsiReaderService;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
public class Q015Service {

    private final List<Q015Packet.Q015> q015List;

    public Q015Service() {
        try {
            Q015Packet packet = NsiReaderService.readNsi(Q015Packet.class);
            q015List = packet.getQ015List();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Q015Packet.Q015> getTestsForType(String typeMd) {
        LocalDate d1 = LocalDate.now();

        return q015List.stream()
                .filter(q015 -> q015.getTypeMd().getTypeD().contains(typeMd))
                .filter(q015 -> q015.getDatebeg().isBefore(d1))
                .filter(q015 -> q015.getDateend() == null || q015.getDateend().isAfter(d1))
                .toList();
    }

}
