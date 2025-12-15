package ru.irkoms.medflk.jaxb;

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
public class MrUslN {

    @XmlElement(name = "MR_N")
    private Integer mrN;

    @XmlElement(name = "PRVS")
    private Integer prvs;

    @XmlElement(name = "CODE_MD")
    private String codeMd;

}
