package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AMrUslN {
    private Integer mrN;
    private Integer prvs;
    private String codeMd;
}
