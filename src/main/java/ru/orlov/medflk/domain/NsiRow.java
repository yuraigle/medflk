package ru.orlov.medflk.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NsiRow {
    private String code;
    private LocalDate date;
    private String version;
    private String description;
}
