package ru.orlov.medflk.jaxb;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class Pacient {

    @NotBlank
    @XmlElement(name = "ID_PAC")
    private String idPac;

    @XmlElement(name = "VPOLIS")
    private Integer vpolis;

    @XmlElement(name = "SPOLIS")
    private String spolis;

    @XmlElement(name = "NPOLIS")
    private String npolis;

    @XmlElement(name = "ST_OKATO")
    private String stOkato;

    @XmlElement(name = "ENP")
    private String enp;

    @XmlElement(name = "SMO")
    private String smo;

    @XmlElement(name = "SMO_OGRN")
    private String smoOgrn;

    @XmlElement(name = "SMO_OK")
    private String smoOk;

    @XmlElement(name = "SMO_NAM")
    private String smoNam;

    @XmlElement(name = "INV")
    private Integer inv;

    @XmlElement(name = "MSE")
    private Integer mse;

    @XmlElement(name = "NOVOR")
    private String novor;

    @XmlElement(name = "VNOV_D")
    private Integer vnovD;

    @XmlElement(name = "SOC")
    private String soc;

    @XmlElement(name = "NEXT_D")
    private Integer nextD;

    @XmlElement(name = "MO_PR")
    private String moPr;

    @XmlElement(name = "VZ")
    private String vz;

}
