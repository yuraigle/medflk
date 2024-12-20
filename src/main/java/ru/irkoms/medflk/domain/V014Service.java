package ru.irkoms.medflk.domain;

import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Setter
@Service
public class V014Service {

    private V014Packet packet;

    public boolean isValidForPomOnDate(Integer forPom, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> !o.getDatebeg().isAfter(d1))
                .filter(o -> o.getDateend() == null || !o.getDateend().isBefore(d1))
                .anyMatch(o -> o.getIdfrmmp().equals(forPom));
    }

}
