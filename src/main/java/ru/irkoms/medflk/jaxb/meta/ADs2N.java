package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ADs2N {
    private String ds2;
    private Integer ds2Pr;
    private Integer prDs2N;
}
