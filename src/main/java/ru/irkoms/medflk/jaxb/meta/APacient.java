package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class APacient {
    private String idPac;
    private Integer vpolis;
    private String spolis;
    private String npolis;
    private String stOkato;
    private String smo;
    private String smoNam;
    private String smoOgrn;
    private String smoOk;
    private String enp;
    private Integer inv;
    private Integer mse;
    private String novor;
    private Integer vnovD;
}
