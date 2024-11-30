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
import lombok.ToString;
import ru.irkoms.medflk.jaxb.meta.*;
import ru.irkoms.medflk.jaxb.util.LocalDateAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@XmlRootElement(name = "ZL_LIST")
@XmlAccessorType(XmlAccessType.NONE)
public class ZlListC extends AZlList {

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
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Zglv extends AZglv {

        @NotNull
        @Size(min = 3, max = 5)
        @XmlElement(name = "VERSION")
        private String version;

        @NotNull
        @PastOrPresent
        @XmlElement(name = "DATA")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate data;

        @NotNull
        @Pattern(regexp = "^[A-Za-z0-9_]{13,26}$")
        @XmlElement(name = "FILENAME")
        private String filename;

        @NotNull
        @Min(1)
        @Max(999_999_999)
        @XmlElement(name = "SD_Z")
        private Integer sdZ;
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Schet extends ASchet {

        @NotNull
        @Min(0)
        @Max(99999999)
        @XmlElement(name = "CODE")
        private Integer code;

        @NotBlank
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

        @NotNull
        @Size(min = 1, max = 15)
        @XmlElement(name = "NSCHET")
        private String nschet;

        @NotNull
        @XmlElement(name = "DSCHET")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate dschet;

        @Size(max = 5)
        @XmlElement(name = "PLAT")
        private String plat;

        @NotNull
        @Digits(integer = 15, fraction = 2)
        @XmlElement(name = "SUMMAV")
        private BigDecimal summav;

        @Size(max = 250)
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

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Zap extends AZap {

        @Min(1)
        @Max(99999999)
        @NotNull
        @XmlElement(name = "N_ZAP")
        private Integer nZap;

        @NotNull
        @Min(0)
        @Max(1)
        @XmlElement(name = "PR_NOV")
        private Integer prNov;

        @Valid
        @NotNull
        @XmlElement(name = "PACIENT")
        private Pacient pacient;

        @Valid
        @NotNull
        @XmlElement(name = "Z_SL")
        private ZSl zSl;

        @Getter
        @Setter
        @ToString
        @EqualsAndHashCode(callSuper = false)
        @XmlAccessorType(XmlAccessType.NONE)
        public static class Pacient extends APacient {

            @NotBlank
            @Size(min = 1, max = 36)
            @XmlElement(name = "ID_PAC")
            private String idPac;

            @NotNull
            @XmlElement(name = "VPOLIS")
            private Integer vpolis;

            @Size(max = 10)
            @XmlElement(name = "SPOLIS")
            private String spolis;

            @Size(max = 20)
            @XmlElement(name = "NPOLIS")
            private String npolis;

            @Pattern(regexp = "|^\\d{5}$", message = "5 цифр")
            @XmlElement(name = "ST_OKATO")
            private String stOkato;

            @Pattern(regexp = "^\\d{5}$", message = "5 цифр")
            @XmlElement(name = "SMO")
            private String smo;

            @Pattern(regexp = "^(|\\d{1,15})$", message = "15 цифр")
            @XmlElement(name = "SMO_OGRN")
            private String smoOgrn;

            @Pattern(regexp = "|[0-9]{16}", message = "16 цифр")
            @XmlElement(name = "ENP")
            private String enp;

            @Pattern(regexp = "|^\\d{5}$", message = "5 цифр")
            @XmlElement(name = "SMO_OK")
            private String smoOk;

            @Size(max = 100)
            @XmlElement(name = "SMO_NAM")
            private String smoNam;

            @Min(0)
            @Max(4)
            @XmlElement(name = "INV")
            private Integer inv;

            @Min(1)
            @Max(1)
            @XmlElement(name = "MSE")
            private Integer mse;

            @NotNull
            @XmlElement(name = "NOVOR")
            private String novor;

            @Min(200)
            @Max(2500)
            @XmlElement(name = "VNOV_D")
            private Integer vnovD;
        }

        @Getter
        @Setter
        @ToString
        @EqualsAndHashCode(callSuper = false)
        @XmlAccessorType(XmlAccessType.NONE)
        public static class ZSl extends AZSl {

            @NotNull
            @Min(0)
            @XmlElement(name = "IDCASE")
            private Integer idcase;

            @NotNull
            @XmlElement(name = "USL_OK")
            private Integer uslOk;

            @NotNull
            @XmlElement(name = "VIDPOM")
            private Integer vidpom;

            @NotNull
            @XmlElement(name = "FOR_POM")
            private Integer forPom;

            @XmlElement(name = "NPR_MO")
            private String nprMo;

            @XmlElement(name = "NPR_DATE")
            @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
            private LocalDate nprDate;

            @NotNull
            @Pattern(regexp = "^[0-9]{6}$", message = "6 цифр")
            @XmlElement(name = "LPU")
            private String lpu;

            @NotNull
            @XmlElement(name = "DATE_Z_1")
            @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
            private LocalDate dateZ1;

            @NotNull
            @XmlElement(name = "DATE_Z_2")
            @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
            private LocalDate dateZ2;

            @Min(0)
            @Max(999)
            @XmlElement(name = "KD_Z")
            private Integer kdZ;

            @XmlElement(name = "VNOV_M")
            private List<@Min(value = 200, message = "Вес VNOV_M не меньше 200") @Max(value = 2500, message = "Вес VNOV_M не больше 2500") Integer> vnovMList;

            @NotNull
            @XmlElement(name = "RSLT")
            private Integer rslt;

            @NotNull
            @XmlElement(name = "ISHOD")
            private Integer ishod;

            @XmlElement(name = "OS_SLUCH")
            private List<@Min(value = 1, message = "Особый случай должен быть (1,2)") @Max(value = 2, message = "Особый случай должен быть (1,2)") Integer> osSluchList;

            @Min(1)
            @Max(1)
            @XmlElement(name = "VB_P")
            private Integer vbP;

            @NotEmpty
            @XmlElement(name = "SL")
            private List<@Valid Sl> slList;

            @XmlElement(name = "IDSP")
            private Integer idsp;

            @NotNull
            @Digits(integer = 15, fraction = 2)
            @XmlElement(name = "SUMV")
            private BigDecimal sumv;

            @Min(0)
            @Max(3)
            @XmlElement(name = "OPLATA")
            private Integer oplata;

            @Digits(integer = 15, fraction = 2)
            @XmlElement(name = "SUMP")
            private BigDecimal sump;

            @XmlElement(name = "SANK")
            private List<@Valid Sank> sankList;

            @XmlElement(name = "SANK_IT")
            private BigDecimal sankIt;

            @Getter
            @Setter
            @ToString
            @EqualsAndHashCode(callSuper = false)
            @XmlAccessorType(XmlAccessType.NONE)
            public static class Sl extends ASl {

                @NotNull
                @Pattern(regexp = "^[0-9a-zA-Z\\-]{1,36}$", message = "До 36 цифр и букв")
                @XmlElement(name = "SL_ID")
                private String slId;

                @XmlElement(name = "LPU_1")
                private String lpu1;

                @XmlElement(name = "PODR")
                private Integer podr;

                @XmlElement(name = "PROFIL")
                private Integer profil;

                @XmlElement(name = "PROFIL_K")
                private Integer profilK;

                @NotNull
                @Min(0)
                @Max(1)
                @XmlElement(name = "DET")
                private Integer det;

                @XmlElement(name = "P_CEL")
                private String pCel;

                @NotNull
                @Size(min = 1, max = 50)
                @XmlElement(name = "NHISTORY")
                private String nhistory;

                @Min(1)
                @Max(4)
                @XmlElement(name = "P_PER")
                private Integer pPer;

                @NotNull
                @XmlElement(name = "DATE_1")
                @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
                private LocalDate date1;

                @NotNull
                @XmlElement(name = "DATE_2")
                @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
                private LocalDate date2;

                @Min(0)
                @Max(999)
                @XmlElement(name = "KD")
                private Integer kd;

                @Size(min = 3, max = 10)
                @XmlElement(name = "DS0")
                private String ds0;

                @NotBlank
                @Size(min = 3, max = 10)
                @XmlElement(name = "DS1")
                private String ds1;

                @XmlElement(name = "DS2")
                private List<String> ds2List;

                @XmlElement(name = "DS3")
                private List<String> ds3List;

                @XmlElement(name = "C_ZAB")
                private Integer cZab;

                @Min(0)
                @Max(1)
                @NotNull
                @XmlElement(name = "DS_ONK")
                private Integer dsOnk;

                @Min(1)
                @Max(9)
                @XmlElement(name = "DN")
                private Integer dn;

                @XmlElement(name = "CODE_MES1")
                private List<@Size(max = 20, message = "CODE_MES1 до 20 знаков") String> codeMes1List;

                @Size(max = 20)
                @XmlElement(name = "CODE_MES2")
                private String codeMes2;

                @Min(1)
                @Max(1)
                @XmlElement(name = "REAB")
                private Integer reab;

                @NotNull
                @XmlElement(name = "PRVS")
                private Integer prvs;

                @NotNull
                // @Pattern(regexp = "^[Vv]021$", message = "должно быть V021")
                @XmlElement(name = "VERS_SPEC")
                private String versSpec;

                @NotBlank
                @Size(max = 25)
                @XmlElement(name = "IDDOKT")
                private String iddokt;

                @Digits(integer = 5, fraction = 2)
                @XmlElement(name = "ED_COL")
                private BigDecimal edCol;

                @Digits(integer = 15, fraction = 2)
                @XmlElement(name = "TARIF")
                private BigDecimal tarif;

                @NotNull
                @Digits(integer = 15, fraction = 2)
                @XmlElement(name = "SUM_M")
                private BigDecimal sumM;

                @Valid
                @XmlElement(name = "KSG_KPG")
                private KsgKpg ksgKpg;

                @Valid
                @XmlElement(name = "ONK_SL")
                private OnkSl onkSl;

                @XmlElement(name = "USL")
                private List<@Valid Usl> uslList;

                @XmlElement(name = "NAPR")
                private List<@Valid Napr> naprList;

                @XmlElement(name = "CONS")
                private List<@Valid Cons> consList;

                @Size(max = 250)
                @XmlElement(name = "COMENTSL")
                private String comentsl;

                @Getter
                @Setter
                @ToString
                @EqualsAndHashCode(callSuper = false)
                @XmlAccessorType(XmlAccessType.NONE)
                public static class Napr extends ANapr {

                    @XmlElement(name = "NAPR_DATE")
                    @XmlJavaTypeAdapter(LocalDateAdapter.class)
                    private LocalDate naprDate;

                    @XmlElement(name = "NAPR_MO")
                    private String naprMo;

                    @XmlElement(name = "NAPR_V")
                    private Integer naprV;

                    @XmlElement(name = "MET_ISSL")
                    private Integer metIssl;

                    @XmlElement(name = "NAPR_USL")
                    private String naprUsl;
                }

                @Getter
                @Setter
                @ToString
                @EqualsAndHashCode(callSuper = false)
                @XmlAccessorType(XmlAccessType.NONE)
                public static class Cons extends ACons {

                    @NotNull
                    @XmlElement(name = "PR_CONS")
                    private Integer prCons;

                    @XmlElement(name = "DT_CONS")
                    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
                    private LocalDate dtCons;

                }

                @Getter
                @Setter
                @ToString
                @EqualsAndHashCode(callSuper = false)
                @XmlAccessorType(XmlAccessType.NONE)
                public static class KsgKpg extends AKsgKpg {

                    @Size(max = 20)
                    @XmlElement(name = "N_KSG")
                    private String nKsg;

                    @NotNull
                    @Pattern(regexp = "^[0-9]{4}$", message = "4 цифры")
                    @XmlElement(name = "VER_KSG")
                    private String verKsg;

                    @NotNull
                    @Min(0)
                    @Max(1)
                    @XmlElement(name = "KSG_PG")
                    private Integer ksgPg;

                    @Size(max = 4)
                    @XmlElement(name = "N_KPG")
                    private String nKpg;

                    @NotNull
                    @Digits(integer = 2, fraction = 6)
                    @XmlElement(name = "KOEF_Z")
                    private BigDecimal koefZ;

                    @NotNull
                    @Digits(integer = 2, fraction = 6)
                    @XmlElement(name = "KOEF_UP")
                    private BigDecimal koefUp;

                    @NotNull
                    @Digits(integer = 6, fraction = 2)
                    @XmlElement(name = "BZTSZ")
                    private BigDecimal bztsz;

                    @NotNull
                    @Digits(integer = 2, fraction = 6)
                    @XmlElement(name = "KOEF_D")
                    private BigDecimal koefD;

                    @NotNull
                    @Digits(integer = 2, fraction = 6)
                    @XmlElement(name = "KOEF_U")
                    private BigDecimal koefU;

                    // @NotNull
                    @DecimalMin("1")
                    @Digits(integer = 2, fraction = 5)
                    @XmlElement(name = "K_ZP")
                    private BigDecimal kZp;

                    @XmlElement(name = "CRIT")
                    private List<@Size(max = 10) String> critList;

                    @NotNull
                    @Min(0)
                    @Max(1)
                    @XmlElement(name = "SL_K")
                    private Integer slK;

                    @Digits(integer = 1, fraction = 5)
                    @XmlElement(name = "IT_SL")
                    private BigDecimal itSl;

                    @XmlElement(name = "SL_KOEF")
                    private List<@Valid SlKoef> slKoefList;

                    @Getter
                    @Setter
                    @ToString
                    @EqualsAndHashCode(callSuper = false)
                    @XmlAccessorType(XmlAccessType.NONE)
                    public static class SlKoef extends ASlKoef {

                        @NotNull
                        @Pattern(regexp = "^[1-9]{1,2}(\\.[1-9])?$", message = "Не соответствует региональному формату X.X")
                        @XmlElement(name = "IDSL")
                        private String idsl;

                        @NotNull
                        @Digits(integer = 1, fraction = 5)
                        @XmlElement(name = "Z_SL")
                        private BigDecimal zSl;
                    }
                }

                @Getter
                @Setter
                @ToString
                @EqualsAndHashCode(callSuper = false)
                @XmlAccessorType(XmlAccessType.NONE)
                public static class Usl extends AUsl {

                    @NotNull
                    @Pattern(regexp = "^[0-9a-zA-Z\\-]{1,36}$", message = "До 36 цифр и букв")
                    @XmlElement(name = "IDSERV")
                    private String idserv;

                    @NotNull
                    @Pattern(regexp = "^[0-9]{6}$", message = "6 цифр")
                    @XmlElement(name = "LPU")
                    private String lpu;

                    @Size(max = 8)
                    @XmlElement(name = "LPU_1")
                    private String lpu1;

                    @Min(0)
                    @Max(999)
                    @XmlElement(name = "PODR")
                    private Integer podr;

                    @NotNull
                    @XmlElement(name = "PROFIL")
                    private Integer profil;

                    @Size(max = 15)
                    @XmlElement(name = "VID_VME")
                    private String vidVme;

                    @NotNull
                    @Min(0)
                    @Max(1)
                    @XmlElement(name = "DET")
                    private Integer det;

                    @NotNull
                    @PastOrPresent
                    @XmlElement(name = "DATE_IN")
                    @XmlJavaTypeAdapter(LocalDateAdapter.class)
                    private LocalDate dateIn;

                    @NotNull
                    @PastOrPresent
                    @XmlElement(name = "DATE_OUT")
                    @XmlJavaTypeAdapter(LocalDateAdapter.class)
                    private LocalDate dateOut;

                    @NotNull
                    @Size(min = 3, max = 10)
                    @XmlElement(name = "DS")
                    private String ds;

                    @NotBlank
                    @Size(max = 20)
                    @XmlElement(name = "CODE_USL")
                    private String codeUsl;

                    @NotNull
                    @Digits(integer = 6, fraction = 2)
                    @XmlElement(name = "KOL_USL")
                    private BigDecimal kolUsl;

                    @Digits(integer = 15, fraction = 2)
                    @XmlElement(name = "TARIF")
                    private BigDecimal tarif;

                    @NotNull
                    @Digits(integer = 15, fraction = 2)
                    @XmlElement(name = "SUMV_USL")
                    private BigDecimal sumvUsl;

                    @XmlElement(name = "PRVS")
                    private Integer prvs;

                    @XmlElement(name = "CODE_MD")
                    private String codeMd;

                    @Min(1)
                    @Max(4)
                    @XmlElement(name = "NPL")
                    private Integer npl;

                    @Size(max = 250)
                    @XmlElement(name = "COMENTU")
                    private String comentu;

                }

                @Getter
                @Setter
                @ToString
                @EqualsAndHashCode(callSuper = false)
                @XmlAccessorType(XmlAccessType.NONE)
                public static class OnkSl extends AOnkSl {

                    @NotNull
                    @XmlElement(name = "DS1_T")
                    private Integer ds1T;

                    @XmlElement(name = "STAD")
                    private Integer stad;

                    @XmlElement(name = "ONK_T")
                    private Integer onkT;

                    @XmlElement(name = "ONK_N")
                    private Integer onkN;

                    @XmlElement(name = "ONK_M")
                    private Integer onkM;

                    @Min(1)
                    @Max(1)
                    @XmlElement(name = "MTSTZ")
                    private Integer mtstz;

                    @Digits(integer = 7, fraction = 2)
                    @XmlElement(name = "SOD")
                    private BigDecimal sod;

                    @Min(0)
                    @Max(99)
                    @XmlElement(name = "K_FR")
                    private Integer kFr;

                    @Digits(integer = 5, fraction = 2)
                    @DecimalMax(value = "500.0", inclusive = false)
                    @XmlElement(name = "WEI")
                    private BigDecimal wei;

                    @Min(10)
                    @Max(259)
                    @XmlElement(name = "HEI")
                    private Integer hei;

                    @DecimalMax(value = "6.0", inclusive = false)
                    @Digits(integer = 5, fraction = 2)
                    @XmlElement(name = "BSA")
                    private BigDecimal bsa;

                    @XmlElement(name = "B_DIAG")
                    private List<@Valid Bdiag> bdiagList;

                    @XmlElement(name = "B_PROT")
                    private List<@Valid Bprot> bprotList;

                    @XmlElement(name = "ONK_USL")
                    private List<@Valid OnkUsl> onkUslList;

                    @Getter
                    @Setter
                    @ToString
                    @EqualsAndHashCode(callSuper = false)
                    @XmlAccessorType(XmlAccessType.NONE)
                    public static class Bdiag extends ABdiag {

                        @NotNull
                        @XmlElement(name = "DIAG_DATE")
                        @XmlJavaTypeAdapter(LocalDateAdapter.class)
                        private LocalDate diagDate;

                        @NotNull
                        @Min(1)
                        @Max(2)
                        @XmlElement(name = "DIAG_TIP")
                        private Integer diagTip;

                        @NotNull
                        @XmlElement(name = "DIAG_CODE")
                        private Integer diagCode;

                        @XmlElement(name = "DIAG_RSLT")
                        private Integer diagRslt;

                        @Min(1)
                        @Max(1)
                        @XmlElement(name = "REC_RSLT")
                        private Integer recRslt;
                    }

                    @Getter
                    @Setter
                    @ToString
                    @EqualsAndHashCode(callSuper = false)
                    @XmlAccessorType(XmlAccessType.NONE)
                    public static class Bprot extends ABprot {

                        @NotNull
                        @XmlElement(name = "PROT")
                        private Integer prot;

                        @NotNull
                        @XmlElement(name = "D_PROT")
                        @XmlJavaTypeAdapter(LocalDateAdapter.class)
                        private LocalDate dProt;

                    }

                    @Getter
                    @Setter
                    @ToString
                    @EqualsAndHashCode(callSuper = false)
                    @XmlAccessorType(XmlAccessType.NONE)
                    public static class OnkUsl extends AOnkUsl {

                        @NotNull
                        @XmlElement(name = "USL_TIP")
                        private Integer uslTip;

                        @XmlElement(name = "HIR_TIP")
                        private Integer hirTip;

                        @XmlElement(name = "LEK_TIP_L")
                        private Integer lekTipL;

                        @XmlElement(name = "LEK_TIP_V")
                        private Integer lekTipV;

                        @XmlElement(name = "LEK_PR")
                        private List<@Valid LekPr> lekPrList;

                        @Min(1)
                        @Max(1)
                        @XmlElement(name = "PPTR")
                        private Integer pptr;

                        @XmlElement(name = "LUCH_TIP")
                        private Integer luchTip;

                        @Getter
                        @Setter
                        @ToString
                        @EqualsAndHashCode(callSuper = false)
                        @XmlAccessorType(XmlAccessType.NONE)
                        public static class LekPr extends ALekPr {

                            @NotNull
                            @PastOrPresent
                            @XmlElement(name = "DATE_INJ")
                            @XmlJavaTypeAdapter(LocalDateAdapter.class)
                            private LocalDate dataInj;

                            @NotBlank
                            @Size(max = 10)
                            @XmlElement(name = "CODE_SH")
                            private String codeSh;

                            @NotBlank
                            @Size(max = 6)
                            @XmlElement(name = "REGNUM")
                            private String regnum;
                        }
                    }
                }
            }
        }
    }
}
