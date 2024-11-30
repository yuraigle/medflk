package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AOnkSl {
    private Integer ds1T;
    private Integer stad;
    private Integer onkT;
    private Integer onkN;
    private Integer onkM;
    private Integer mtstz;
    private BigDecimal sod;
    private Integer kFr;
    private BigDecimal wei;
    private Integer hei;
    private BigDecimal bsa;

    private List<? extends ABdiag> bdiagList;

    private List<? extends ABprot> bprotList;

    private List<? extends AOnkUsl> onkUslList;
}
