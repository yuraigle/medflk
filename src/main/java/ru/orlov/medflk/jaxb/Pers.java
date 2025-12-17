package ru.orlov.medflk.jaxb;

import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class Pers {

    @NotBlank
    @XmlElement(name = "ID_PAC")
    private String idPac;

    @XmlElement(name = "FAM")
    private String fam;

    @XmlElement(name = "IM")
    private String im;

    @XmlElement(name = "OT")
    private String ot;

    @NotNull
    @XmlElement(name = "W")
    private Integer w;

    @XmlElement(name = "DR")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dr;

    @XmlElement(name = "DOST")
    private List<Integer> dostList;

    @XmlElement(name = "TEL")
    private String tel;

    @XmlElement(name = "FAM_P")
    private String famP;

    @XmlElement(name = "IM_P")
    private String imP;

    @XmlElement(name = "OT_P")
    private String otP;

    @XmlElement(name = "W_P")
    private Integer wP;

    @XmlElement(name = "DR_P")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate drP;

    @XmlElement(name = "DOST_P")
    private List<Integer> dostPList;

    @XmlElement(name = "MR")
    private String mr;

    @XmlElement(name = "DOCTYPE")
    private Integer docType;

    @XmlElement(name = "DOCSER")
    private String docSer;

    @XmlElement(name = "DOCNUM")
    private String docNum;

    @XmlElement(name = "DOCDATE")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate docDate;

    @XmlElement(name = "DOCORG")
    private String docOrg;

    @XmlElement(name = "SNILS")
    private String snils;

    @XmlElement(name = "OKATOG")
    private String okatoG;

    @XmlElement(name = "OKATOP")
    private String okatoP;

    @XmlElement(name = "COMENTP")
    private String comentp;

}
