package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AZap {
    private Integer nZap;
    private Integer prNov;
    private APacient pacient;
    private AZSl zSl;
}
