package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class APersList {
    private AZglv zglv;

    private List<? extends APers> persList;
}
