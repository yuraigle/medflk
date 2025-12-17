package ru.orlov.medflk.jaxb;

import jakarta.validation.constraints.*;
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
public class Schet {

    @XmlElement(name = "CODE")
    private Integer code;

    @XmlElement(name = "CODE_MO")
    private String codeMo;

    @XmlElement(name = "YEAR")
    private Integer year;

    @XmlElement(name = "MONTH")
    private Integer month;

    @XmlElement(name = "NSCHET")
    private String nschet;

    @XmlElement(name = "DSCHET")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dschet;

    @XmlElement(name = "PLAT")
    private String plat;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SUMMAV")
    private BigDecimal summav;

    @XmlElement(name = "COMENTS")
    private String coments;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SUMMAP")
    private BigDecimal summap;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SANK_MEK")
    private BigDecimal sankMek;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SANK_MEE")
    private BigDecimal sankMee;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SANK_EKMP")
    private BigDecimal sankEkmp;

    @XmlElement(name = "DISP")
    private String disp;

}
