package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ABprot {
    private LocalDate dProt;
    private Integer prot;
}
