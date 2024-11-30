package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AUsl {
    private String idserv;
    private String lpu;
    private String lpu1;
    private Integer podr;
    private Integer profil;
    private String vidVme;
    private Integer det;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private String ds;
    private String codeUsl;
    private BigDecimal kolUsl;
    private BigDecimal uetUsl;
    private Integer prvd;
    private BigDecimal tarif;
    private BigDecimal sumvUsl;
    private Integer prvs;
    private String codeMd;
    private Integer npl;
    private String comentu;
    private Integer pOtk;

    private List<? extends AMrUslN> mrUslNList;

    private List<? extends AMedDev> medDevList;
}
