package ru.orlov.medflk.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateAdapter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class Naz {

    @XmlElement(name = "NAZ_N")
    private Integer nazN;

    @XmlElement(name = "NAZ_R")
    private Integer nazR;

    @XmlElement(name = "NAZ_IDDOKT")
    private String nazIddokt;

    @XmlElement(name = "NAZ_V")
    private Integer nazV;

    @XmlElement(name = "NAZ_USL")
    private String nazUsl;

    @XmlElement(name = "NAPR_DATE")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate naprDate;

    @XmlElement(name = "NAPR_MO")
    private String naprMo;

    @XmlElement(name = "NAZ_PMP")
    private Integer nazPmp;

    @XmlElement(name = "NAZ_PK")
    private Integer nazPk;

}
