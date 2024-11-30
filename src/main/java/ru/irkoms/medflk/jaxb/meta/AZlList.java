package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AZlList {
    private AZglv zglv;
    private ASchet schet;

    private List<? extends AZap> zapList;
}
