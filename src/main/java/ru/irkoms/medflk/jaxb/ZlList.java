package ru.irkoms.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.irkoms.medflk.jaxb.util.LocalDateAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@XmlRootElement(name = "ZL_LIST")
@XmlAccessorType(XmlAccessType.NONE)
public class ZlList {

    @Valid
    @NotNull
    @XmlElement(name = "ZGLV")
    private Zglv zglv;

    @Valid
    @NotNull
    @XmlElement(name = "SCHET")
    private Schet schet;

    @NotEmpty
    @XmlElement(name = "ZAP")
    private List<@Valid Zap> zapList;

    @Getter
    @Setter
    @EqualsAndHashCode
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Zglv {

        @XmlElement(name = "VERSION")
        private String version;

        @XmlElement(name = "DATA")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate data;

        @XmlElement(name = "FILENAME")
        private String filename;

        @XmlElement(name = "SD_Z")
        private Integer sdZ;
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Schet {

        @XmlElement(name = "CODE")
        private Integer code;

        @NotNull
        @Size(min = 6, max = 6)
        @XmlElement(name = "CODE_MO")
        private String codeMo;

        @NotNull
        @Min(2011)
        @Max(2050)
        @XmlElement(name = "YEAR")
        private Integer year;

        @NotNull
        @Min(1)
        @Max(12)
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
    }
}
