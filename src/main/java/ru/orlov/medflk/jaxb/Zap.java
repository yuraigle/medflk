package ru.orlov.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class Zap {

    @NotNull
    @XmlElement(name = "N_ZAP")
    private Integer nZap;

    @XmlElement(name = "PR_NOV")
    private Integer prNov;

    @Valid
    @NotNull
    @XmlElement(name = "PACIENT")
    private Pacient pacient;

    @Valid
    @NotNull
    @XmlElement(name = "Z_SL")
    private ZSl zSl;
}
