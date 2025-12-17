package ru.orlov.medflk.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateAdapter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class Zglv {

    @XmlElement(name = "VERSION")
    private String version;

    @XmlElement(name = "DATA")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate data;

    @XmlElement(name = "FILENAME")
    private String filename;

    @XmlElement(name = "FILENAME1")
    private String filename1;

    @XmlElement(name = "SD_Z")
    private Integer sdZ;

}
