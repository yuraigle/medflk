package ru.orlov.medflk.jaxb;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class Inj {

    @XmlElement(name = "S_INJ")
    private BigDecimal sInj;

    @XmlElement(name = "DATE_INJ")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateInj;

    @XmlElement(name = "KV_INJ")
    private BigDecimal kvInj;

    @XmlElement(name = "KIZ_INJ")
    private BigDecimal kizInj;

    @XmlElement(name = "SV_INJ")
    private BigDecimal svInj;

    @XmlElement(name = "SIZ_INJ")
    private BigDecimal sizInj;

    @XmlElement(name = "RED_INJ")
    private Integer redInj;

}
