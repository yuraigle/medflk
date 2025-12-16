package ru.orlov.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class ZSl {

    @NotNull
    @XmlElement(name = "IDCASE")
    private Integer idcase;

    @XmlElement(name = "USL_OK")
    private Integer uslOk;

    @XmlElement(name = "VIDPOM")
    private Integer vidpom;

    @XmlElement(name = "FOR_POM")
    private Integer forPom;

    @XmlElement(name = "NPR_MO")
    private String nprMo;

    @XmlElement(name = "NPR_DATE")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate nprDate;

    @XmlElement(name = "LPU")
    private String lpu;

    @XmlElement(name = "VBR")
    private Integer vbr;

    @NotNull
    @PastOrPresent
    @XmlElement(name = "DATE_Z_1")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateZ1;

    @NotNull
    @XmlElement(name = "DATE_Z_2")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateZ2;

    @XmlElement(name = "P_OTK")
    private Integer pOtk;

    @XmlElement(name = "KD_Z")
    private Integer kdZ;

    @XmlElement(name = "VNOV_M")
    private List<Integer> vnovMList;

    @XmlElement(name = "RSLT")
    private Integer rslt;

    @XmlElement(name = "RSLT_D")
    private Integer rsltD;

    @XmlElement(name = "ISHOD")
    private Integer ishod;

    @XmlElement(name = "OS_SLUCH")
    private List<Integer> osSluchList;

    @XmlElement(name = "VB_P")
    private Integer vbP;

    @NotEmpty
    @XmlElement(name = "SL")
    private List<Sl> slList;

    @XmlElement(name = "IDSP")
    private Integer idsp;

    @NotNull
    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SUMV")
    private BigDecimal sumv;

    @XmlElement(name = "OPLATA")
    private Integer oplata;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SUMP")
    private BigDecimal sump;

    @XmlElement(name = "SANK")
    private List<@Valid Sank> sankList;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SANK_IT")
    private BigDecimal sankIt;

}
