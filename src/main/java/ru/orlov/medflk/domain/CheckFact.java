package ru.orlov.medflk.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class CheckFact {

    private String test;
    private String element;
    private String description;
    private String result;

}
