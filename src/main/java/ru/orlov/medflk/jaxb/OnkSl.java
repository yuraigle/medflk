package ru.orlov.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class OnkSl {

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

    @XmlElement(name = "MTSTZ")
    private Integer mtstz;

    @Digits(integer = 7, fraction = 2)
    @XmlElement(name = "SOD")
    private BigDecimal sod;

    @XmlElement(name = "K_FR")
    private Integer kFr;

    @Digits(integer = 5, fraction = 2)
    @XmlElement(name = "WEI")
    private BigDecimal wei;

    @XmlElement(name = "HEI")
    private Integer hei;

    @Digits(integer = 5, fraction = 2)
    @XmlElement(name = "BSA")
    private BigDecimal bsa;

    @XmlElement(name = "B_DIAG")
    private List<@Valid Bdiag> bdiagList;

    @XmlElement(name = "B_PROT")
    private List<@Valid Bprot> bprotList;

    @XmlElement(name = "ONK_USL")
    private List<@Valid OnkUsl> onkUslList;

}
