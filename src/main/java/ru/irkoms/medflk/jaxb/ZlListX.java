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
public class ZlListX extends AZlList {

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

        // @NotBlank // не буду требовать
        @Size(max = 3)
        @XmlElement(name = "DISP")
        private String disp;
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Zap extends AZap {

        @NotNull
        @Min(1)
        @Max(99999999)
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

            @NotNull
            @XmlElement(name = "NOVOR")
            private String novor;
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
            @XmlElement(name = "VIDPOM")
            private Integer vidpom;

            @NotNull
            @Pattern(regexp = "^[0-9]{6}$", message = "6 цифр")
            @XmlElement(name = "LPU")
            private String lpu;

            @Min(0)
            @Max(1)
            @NotNull
            @XmlElement(name = "VBR")
            private Integer vbr;

            @NotNull
            @XmlElement(name = "DATE_Z_1")
            @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
            private LocalDate dateZ1;

            @NotNull
            @XmlElement(name = "DATE_Z_2")
            @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
            private LocalDate dateZ2;

            @NotNull
            @Min(0)
            @Max(1)
            @XmlElement(name = "P_OTK")
            private Integer pOtk;

            @Max(99)
            @XmlElement(name = "RSLT_D")
            private Integer rsltD;

            @XmlElement(name = "OS_SLUCH")
            private List<@Min(value = 1, message = "Особый случай должен быть (1,2)") @Max(value = 2, message = "Особый случай должен быть (1,2)") Integer> osSluchList;

            @NotEmpty
            @Size(max = 1)
            @XmlElement(name = "SL")
            private List<@Valid Sl> slList;

            @Max(99)
            @NotNull
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

            @Digits(integer = 15, fraction = 2)
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

                @NotNull
                @Size(min = 1, max = 50)
                @XmlElement(name = "NHISTORY")
                private String nhistory;

                // в приложениях ТФОМСа это поле тут, а не в заголовке
                @Size(max = 3)
                @XmlElement(name = "DISP")
                private String disp;

                @NotNull
                @XmlElement(name = "DATE_1")
                @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
                private LocalDate date1;

                @NotNull
                @XmlElement(name = "DATE_2")
                @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
                private LocalDate date2;

                // Необязательный в X
                @Size(min = 3, max = 10)
                @XmlElement(name = "DS1")
                private String ds1;

                @Min(1)
                @Max(1)
                @XmlElement(name = "DS1_PR")
                private Integer ds1Pr;

                @Min(0)
                @Max(1)
                @NotNull
                @XmlElement(name = "DS_ONK")
                private Integer dsOnk;

                @Min(1)
                @Max(3)
                @XmlElement(name = "PR_D_N")
                private Integer prDN;

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

                @NotNull
                @Digits(integer = 15, fraction = 2)
                @XmlElement(name = "SUM_M")
                private BigDecimal sumM;

                @XmlElement(name = "USL")
                private List<@Valid Usl> uslList;

                @Size(max = 250)
                @XmlElement(name = "COMENTSL")
                private String comentsl;

                @Getter
                @Setter
                @ToString
                @EqualsAndHashCode(callSuper = false)
                @XmlAccessorType(XmlAccessType.NONE)
                public static class Ds2N extends ADs2N {

                    @NotNull
                    @Size(min = 3, max = 10)
                    @XmlElement(name = "DS2")
                    private String ds2;

                    //                    @Min(1)
                    @Max(1)
                    @XmlElement(name = "DS2_PR")
                    private Integer ds2Pr;

                    //                    @Min(1)
                    @Max(3)
//                    @NotNull // wtf ГН не заполняет
                    @XmlElement(name = "PR_DS2_N")
                    private Integer prDs2N;
                }

                @Getter
                @Setter
                @ToString
                @EqualsAndHashCode(callSuper = false)
                @XmlAccessorType(XmlAccessType.NONE)
                public static class Naz extends ANaz {

                    @NotNull
                    @Min(1)
                    @Max(99)
                    @XmlElement(name = "NAZ_N")
                    private Integer nazN;

                    @Min(1)
                    @Max(6)
                    @XmlElement(name = "NAZ_R")
                    private Integer nazR;

                    @XmlElement(name = "NAZ_SP")
                    private Integer nazSp;

                    @XmlElement(name = "NAZ_IDDOKT")
                    private String nazIddokt;

                    @XmlElement(name = "NAZ_V")
                    private Integer nazV;

                    @Size(max = 15)
                    @XmlElement(name = "NAZ_USL")
                    private String nazUsl;

                    @XmlElement(name = "NAPR_DATE")
                    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
                    private LocalDate naprDate;

                    @Size(max = 6)
                    @XmlElement(name = "NAPR_MO")
                    private String naprMo;

                    @XmlElement(name = "NAZ_PMP")
                    private Integer nazPmp;

                    @XmlElement(name = "NAZ_PK")
                    private Integer nazPk;
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

                    @XmlElement(name = "LPU_1")
                    private String lpu1;

                    @NotNull
                    @XmlElement(name = "DATE_IN")
                    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
                    private LocalDate dateIn;

                    @NotNull
                    @XmlElement(name = "DATE_OUT")
                    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
                    private LocalDate dateOut;

                    @NotNull
                    @Min(0)
                    @Max(1)
                    @XmlElement(name = "P_OTK")
                    private Integer pOtk;

                    @XmlElement(name = "CODE_USL")
                    private String codeUsl;

                    @Digits(integer = 15, fraction = 2)
                    @XmlElement(name = "TARIF")
                    private BigDecimal tarif;

                    @NotNull
                    @Digits(integer = 15, fraction = 2)
                    @XmlElement(name = "SUMV_USL")
                    private BigDecimal sumvUsl;

                    @XmlElement(name = "MR_USL_N")
                    private List<@Valid MrUslN> mrUslNList;

                    @XmlElement(name = "COMENTU")
                    private String comentu;

                    @Getter
                    @Setter
                    @ToString
                    @EqualsAndHashCode(callSuper = false)
                    @XmlAccessorType(XmlAccessType.NONE)
                    public static class MrUslN extends AMrUslN {

                        @XmlElement(name = "MR_N")
                        private Integer mrN;

                        @XmlElement(name = "PRVS")
                        private Integer prvs;

                        @XmlElement(name = "CODE_MD")
                        private String codeMd;
                    }
                }
            }
        }
    }
}
