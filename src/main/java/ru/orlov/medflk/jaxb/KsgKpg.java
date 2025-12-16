package ru.orlov.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class KsgKpg {

    @XmlElement(name = "N_KSG")
    private String nKsg;

    @XmlElement(name = "VER_KSG")
    private String verKsg;

    @XmlElement(name = "KSG_PG")
    private Integer ksgPg;

    @XmlElement(name = "N_KPG")
    private String nKpg;

    @Digits(integer = 2, fraction = 6)
    @XmlElement(name = "KOEF_Z")
    private BigDecimal koefZ;

    @Digits(integer = 2, fraction = 6)
    @XmlElement(name = "KOEF_UP")
    private BigDecimal koefUp;

    @Digits(integer = 6, fraction = 2)
    @XmlElement(name = "BZTSZ")
    private BigDecimal bztsz;

    @Digits(integer = 2, fraction = 6)
    @XmlElement(name = "KOEF_D")
    private BigDecimal koefD;

    @Digits(integer = 2, fraction = 6)
    @XmlElement(name = "KOEF_U")
    private BigDecimal koefU;

    @Digits(integer = 2, fraction = 5)
    @XmlElement(name = "K_ZP")
    private BigDecimal kZp;

    @XmlElement(name = "CRIT")
    private List<String> critList;

    @XmlElement(name = "SL_K")
    private Integer slK;

    @Digits(integer = 1, fraction = 5)
    @XmlElement(name = "IT_SL")
    private BigDecimal itSl;

    @XmlElement(name = "SL_KOEF")
    private List<@Valid SlKoef> slKoefList;

    @Getter
    @Setter
    @EqualsAndHashCode
    @XmlAccessorType(XmlAccessType.NONE)
    public static class SlKoef {

        @XmlElement(name = "IDSL")
        private String idsl;

        @Digits(integer = 1, fraction = 5)
        @XmlElement(name = "Z_SL")
        private BigDecimal zSl;
    }
}
