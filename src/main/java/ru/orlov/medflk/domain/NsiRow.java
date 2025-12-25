package ru.orlov.medflk.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NsiRow {
    private String code;
    private LocalDate date;
}
