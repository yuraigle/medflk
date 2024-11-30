package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ASank {
    private String sCode;
    private BigDecimal sSum;
    private Integer sTip;
    private Integer sOsn;
    private LocalDate dateAct;
    private String numAct;
    private String sCom;
    private Integer sIst;

    private List<String> slIdList;
    private List<String> codeExpList;
}
