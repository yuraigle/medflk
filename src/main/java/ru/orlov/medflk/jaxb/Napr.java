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
public class Napr {

    @XmlElement(name = "NAPR_DATE")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate naprDate;

    @XmlElement(name = "NAPR_MO")
    private String naprMo;

    @XmlElement(name = "NAPR_V")
    private Integer naprV;

    @XmlElement(name = "MET_ISSL")
    private Integer metIssl;

    @XmlElement(name = "NAPR_USL")
    private String naprUsl;
}
