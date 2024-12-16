package ru.irkoms.medflk.domain;

import org.springframework.stereotype.Service;
import ru.irkoms.medflk.service.NsiReaderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class F011Service {

    private final List<F011Packet.F011> f011List = new ArrayList<>();

    private List<F011Packet.F011> readNsi() {
        if (f011List.isEmpty()) {
            try {
                F011Packet packet = NsiReaderService.readNsi(F011Packet.class, "nsi/F011.ZIP");
                f011List.addAll(packet.getZapList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return f011List;
    }

    public boolean isValidIdDocOnDate(Integer idDoc, LocalDate d1) {
        return readNsi().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdDoc().equals(idDoc));
    }

}
