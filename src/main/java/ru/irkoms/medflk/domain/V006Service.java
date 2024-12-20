package ru.irkoms.medflk.domain;

import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Setter
@Service
public class V006Service {

    private V006Packet packet;

    public boolean isValidUslOkOnDate(Integer uslOk, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdump().equals(uslOk));
    }

}
