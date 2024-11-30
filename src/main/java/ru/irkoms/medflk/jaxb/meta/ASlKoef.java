package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ASlKoef {
    private String idsl;
    private BigDecimal zSl;
}
