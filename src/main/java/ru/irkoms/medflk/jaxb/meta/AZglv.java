package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AZglv {
    private String version;
    private LocalDate data;
    private String filename;
    private String filename1;
    private Integer sdZ;

    public abstract void setFilename(String filename);

    public abstract void setData(LocalDate data);

}
