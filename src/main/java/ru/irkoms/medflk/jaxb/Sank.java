package ru.irkoms.medflk.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.irkoms.medflk.jaxb.util.LocalDateAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@XmlAccessorType(XmlAccessType.NONE)
public class Sank {

    @XmlElement(name = "S_CODE")
    private String sCode;

    @XmlElement(name = "S_SUM")
    private BigDecimal sSum;

    @XmlElement(name = "S_TIP")
    private Integer sTip;

    @XmlElement(name = "SL_ID")
    private List<String> slIdList;

    @XmlElement(name = "S_OSN")
    private Integer sOsn;

    @XmlElement(name = "DATE_ACT")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dateAct;

    @XmlElement(name = "NUM_ACT")
    private String numAct;

    @XmlElement(name = "CODE_EXP")
    private List<String> codeExpList;

    @XmlElement(name = "S_COM")
    private String sCom;

    @XmlElement(name = "S_IST")
    private Integer sIst;

}
