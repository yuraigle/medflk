package ru.orlov.medflk.jaxb;

import jakarta.validation.Valid;
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

    @XmlElement(name = "KOEF_Z")
    private BigDecimal koefZ;

    @XmlElement(name = "KOEF_UP")
    private BigDecimal koefUp;

    @XmlElement(name = "BZTSZ")
    private BigDecimal bztsz;

    @XmlElement(name = "KOEF_D")
    private BigDecimal koefD;

    @XmlElement(name = "KOEF_U")
    private BigDecimal koefU;

    @XmlElement(name = "K_ZP")
    private BigDecimal kZp;

    @XmlElement(name = "CRIT")
    private List<String> critList;

    @XmlElement(name = "SL_K")
    private Integer slK;

    @XmlElement(name = "IT_SL")
    private BigDecimal itSl;

    @XmlElement(name = "SL_KOEF")
    private List<@Valid SlKoef> slKoefList;

    @XmlElement(name = "PR_PR")
    private String prPr;

    @XmlElement(name = "KOEF_PR")
    private BigDecimal koefPr;

    @Getter
    @Setter
    @EqualsAndHashCode
    @XmlAccessorType(XmlAccessType.NONE)
    public static class SlKoef {

        @XmlElement(name = "IDSL")
        private String idsl;

        @XmlElement(name = "Z_SL")
        private BigDecimal zSl;
    }
}
