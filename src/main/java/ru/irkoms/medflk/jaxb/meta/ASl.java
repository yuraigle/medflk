package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ASl {
    private String slId;
    private String vidHmp;
    private Integer metodHmp;
    private Integer groupHmp;
    private String lpu1;
    private Integer podr;
    private Integer profil;
    private Integer profilK;
    private Integer det;
    private LocalDate talD;
    private String talNum;
    private LocalDate talP;
    private String pCel;
    private String nhistory;
    private String disp; // regional
    private Integer pPer;
    private LocalDate date1;
    private LocalDate date2;
    private Integer kd;
    private String ds0;
    private String ds1;
    private Integer ds1Pr;
    private Integer dsOnk;
    private Integer prDN;
    private List<String> ds2List;
    private List<String> ds3List;
    private Integer cZab;
    private Integer dn;
    private List<String> codeMes1List;
    private String codeMes2;
    private Integer reab;
    private Integer prvs;
    private String versSpec;
    private String iddokt;
    private BigDecimal edCol;
    private BigDecimal tarif;
    private BigDecimal sumM;
    private String comentsl;
    private BigDecimal wei;
    private AKsgKpg ksgKpg;
    private AOnkSl onkSl;

    private List<? extends ADs2N> ds2NList;

    private List<? extends ANaz> nazList;

    private List<? extends ANapr> naprList;

    private List<? extends ACons> consList;

    private List<? extends AUsl> uslList;

    private List<? extends ALekPr> lekPrList;
}
