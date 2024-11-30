package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AOnkUsl {
    private Integer uslTip;
    private Integer hirTip;
    private Integer lekTipL;
    private Integer lekTipV;
    private Integer pptr;
    private Integer luchTip;

    private List<? extends ALekPr> lekPrList;
}
