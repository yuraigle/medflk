package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ANaz {
    private Integer nazN;
    private Integer nazR;
    private Integer nazSp;
    private String nazIddokt;
    private Integer nazV;
    private String nazUsl;
    private LocalDate naprDate;
    private String naprMo;
    private Integer nazPmp;
    private Integer nazPk;
}
