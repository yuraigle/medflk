package ru.orlov.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@XmlRootElement(name = "ZL_LIST")
@XmlAccessorType(XmlAccessType.NONE)
public class ZlList {

    @Valid
    @NotNull
    @XmlElement(name = "ZGLV")
    private Zglv zglv;

    @Valid
    @NotNull
    @XmlElement(name = "SCHET")
    private Schet schet;

    @NotEmpty
    @XmlElement(name = "ZAP")
    private List<@Valid Zap> zapList;

}
