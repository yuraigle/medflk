package ru.irkoms.medflk.domain;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Setter
@Service
public class F032Service {

    private F032Packet packet;

    @Value("${regional.okato}")
    private String regionalOkato;

    public boolean isValidCodeMoOnDate(String codeMo, LocalDate d1) {
        return packet.getZapList().stream()
                .anyMatch(o -> o.getMcod().equals(codeMo));
    }

    public boolean isValidRegionalCodeMoOnDate(String codeMo, LocalDate d1) {
        return packet.getZapList().stream()
                .filter(o -> o.getOktmoP().startsWith(regionalOkato))
                .anyMatch(o -> o.getMcod().equals(codeMo));
    }
}
