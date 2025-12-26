package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateRusAdapter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class NsiDefaultZglv {

    @XmlElement(name = "version")
    private String version;

    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
    private LocalDate date;

}
