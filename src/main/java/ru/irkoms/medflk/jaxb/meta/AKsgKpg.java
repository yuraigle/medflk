package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AKsgKpg {
    private String nKsg;
    private String verKsg;
    private Integer ksgPg;
    private String nKpg;
    private BigDecimal koefZ;
    private BigDecimal koefUp;
    private BigDecimal bztsz;
    private BigDecimal koefD;
    private BigDecimal koefU;
    private List<String> critList;
    private Integer slK;
    private BigDecimal itSl;

    private List<? extends ASlKoef> slKoefList;
}
