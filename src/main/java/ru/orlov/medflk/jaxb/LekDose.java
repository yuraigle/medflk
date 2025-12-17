package ru.orlov.medflk.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class LekDose {

    @XmlElement(name = "ED_IZM")
    private String edIzm;

    @XmlElement(name = "DOSE_INJ")
    private BigDecimal doseInj;

    @XmlElement(name = "METHOD_INJ")
    private String methodInj;

    @XmlElement(name = "COL_INJ")
    private Integer colInj;

}
