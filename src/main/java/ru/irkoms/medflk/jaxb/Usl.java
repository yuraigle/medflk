package ru.irkoms.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
@XmlAccessorType(XmlAccessType.NONE)
public class Usl {

    @NotNull
    @XmlElement(name = "IDSERV")
    private String idserv;

    @XmlElement(name = "LPU")
    private String lpu;

    @XmlElement(name = "LPU_1")
    private String lpu1;

    @XmlElement(name = "PODR")
    private Integer podr;

    @XmlElement(name = "PROFIL")
    private Integer profil;

    @XmlElement(name = "VID_VME")
    private String vidVme;

    @XmlElement(name = "DET")
    private Integer det;

    @XmlElement(name = "DATE_IN")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateIn;

    @XmlElement(name = "DATE_OUT")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateOut;

    @XmlElement(name = "DS")
    private String ds;

    @XmlElement(name = "CODE_USL")
    private String codeUsl;

    @Digits(integer = 6, fraction = 2)
    @XmlElement(name = "KOL_USL")
    private BigDecimal kolUsl;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "TARIF")
    private BigDecimal tarif;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SUMV_USL")
    private BigDecimal sumvUsl;

    @XmlElement(name = "PRVS")
    private Integer prvs;

    @XmlElement(name = "CODE_MD")
    private String codeMd;

    @XmlElement(name = "MR_USL_N")
    private List<@Valid MrUslN> mrUslNList;

    @XmlElement(name = "NPL")
    private Integer npl;

    @XmlElement(name = "COMENTU")
    private String comentu;

    @XmlElement(name = "MED_DEV")
    private List<@Valid MedDev> medDevList;

}
