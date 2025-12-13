package ru.irkoms.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class OnkUsl {

    @XmlElement(name = "USL_TIP")
    private Integer uslTip;

    @XmlElement(name = "HIR_TIP")
    private Integer hirTip;

    @XmlElement(name = "LEK_TIP_L")
    private Integer lekTipL;

    @XmlElement(name = "LEK_TIP_V")
    private Integer lekTipV;

    @XmlElement(name = "LEK_PR")
    private List<@Valid LekPrOnk> lekPrList;

    @XmlElement(name = "PPTR")
    private Integer pptr;

    @XmlElement(name = "LUCH_TIP")
    private Integer luchTip;

}
