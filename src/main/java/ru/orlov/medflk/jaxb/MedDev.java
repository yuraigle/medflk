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
public class MedDev {

    @XmlElement(name = "DATE_MED")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dateMed;

    @XmlElement(name = "CODE_MEDDEV")
    private Integer codeMeddev;

    @XmlElement(name = "NUMBER_SER")
    private String numberSer;

}
