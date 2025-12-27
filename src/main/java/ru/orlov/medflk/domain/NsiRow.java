package ru.orlov.medflk.domain;

import lombok.Data;
import ru.orlov.medflk.domain.nsi.AbstractNsiService;

import java.time.LocalDate;

@Data
public class NsiRow {
    private String code;
    private LocalDate date;
    private String version;
    private String description;

    public NsiRow(AbstractNsiService nsi) {
        setCode(nsi.getClass().getSimpleName().substring(0, 4));
        setDate(nsi.getDate());
        setVersion(nsi.getVersion());
        setDescription(nsi.getDescription());
    }
}
