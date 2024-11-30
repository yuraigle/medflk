package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ALekPr {
    private LocalDate dataInj;
    private String codeSh;
    private String regnum;
    private String codMark;
    private ALekDose lekDose;
}
