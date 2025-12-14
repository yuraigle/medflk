package ru.irkoms.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class Sl {

    @NotNull
    @XmlElement(name = "SL_ID")
    private String slId;

    @XmlElement(name = "VID_HMP")
    private String vidHmp;

    @XmlElement(name = "METOD_HMP")
    private Integer metodHmp;

    @XmlElement(name = "LPU_1")
    private String lpu1;

    @XmlElement(name = "PODR")
    private Integer podr;

    @XmlElement(name = "PROFIL")
    private Integer profil;

    @XmlElement(name = "PROFIL_K")
    private Integer profilK;

    @XmlElement(name = "DET")
    private Integer det;

    @XmlElement(name = "P_CEL")
    private String pCel;

    @XmlElement(name = "TAL_D")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate talD;

    @XmlElement(name = "TAL_NUM")
    private String talNum;

    @XmlElement(name = "TAL_P")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate talP;

    @XmlElement(name = "NHISTORY")
    private String nhistory;

    @XmlElement(name = "P_PER")
    private Integer pPer;

    @NotNull
    @XmlElement(name = "DATE_1")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date1;

    @NotNull
    @XmlElement(name = "DATE_2")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date2;

    @XmlElement(name = "KD")
    private Integer kd;

    @XmlElement(name = "DS0")
    private String ds0;

    @NotBlank
    @XmlElement(name = "DS1")
    private String ds1;

    @XmlElement(name = "DS2")
    private List<String> ds2List;

    @XmlElement(name = "DS3")
    private List<String> ds3List;

    @XmlElement(name = "C_ZAB")
    private Integer cZab;

    @XmlElement(name = "DS_ONK")
    private Integer dsOnk;

    @XmlElement(name = "DN")
    private Integer dn;

    @XmlElement(name = "PR_D_N")
    private Integer prDN;


    @XmlElement(name = "CODE_MES1")
    private List<String> codeMes1List;

    @Size(max = 20)
    @XmlElement(name = "CODE_MES2")
    private String codeMes2;

    @Valid
    @XmlElement(name = "KSG_KPG")
    private KsgKpg ksgKpg;

    @Valid
    @XmlElement(name = "ONK_SL")
    private OnkSl onkSl;

    @XmlElement(name = "REAB")
    private Integer reab;

    @XmlElement(name = "PRVS")
    private Integer prvs;

    @XmlElement(name = "VERS_SPEC")
    private String versSpec;

    @XmlElement(name = "IDDOKT")
    private String iddokt;

    @XmlElement(name = "DS2_N")
    private List<@Valid Ds2N> ds2NList;

    @XmlElement(name = "NAZ")
    private List<@Valid Naz> nazList;

    @Digits(integer = 5, fraction = 2)
    @XmlElement(name = "ED_COL")
    private BigDecimal edCol;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "TARIF")
    private BigDecimal tarif;

    @Digits(integer = 15, fraction = 2)
    @XmlElement(name = "SUM_M")
    private BigDecimal sumM;

    @XmlElement(name = "USL")
    private List<Usl> uslList;

    @XmlElement(name = "NAPR")
    private List<@Valid Napr> naprList;

    @XmlElement(name = "CONS")
    private List<@Valid Cons> consList;

    @XmlElement(name = "COMENTSL")
    private String comentsl;

    @Digits(integer = 3, fraction = 1)
    @XmlElement(name = "WEI")
    private BigDecimal wei;

    @XmlElement(name = "LEK_PR")
    private List<LekPrOnk> lekPrList;

}
