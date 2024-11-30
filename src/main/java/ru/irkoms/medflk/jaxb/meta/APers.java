package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class APers {
    private String idPac;
    private String fam;
    private String im;
    private String ot;
    private Integer w;
    private LocalDate dr;
    private List<Integer> dostList;
    private String tel;
    private String famP;
    private String imP;
    private String otP;
    private Integer wP;
    private LocalDate drP;
    private List<Integer> dostPList;
    private String mr;
    private Integer docType;
    private String docSer;
    private String docNum;
    private LocalDate docDate;
    private String docOrg;
    private String snils;
    private String okatoG;
    private String okatoP;
    private String comentp;
}
