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
public class Bdiag {

    @XmlElement(name = "DIAG_DATE")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate diagDate;

    @XmlElement(name = "DIAG_TIP")
    private Integer diagTip;

    @XmlElement(name = "DIAG_CODE")
    private Integer diagCode;

    @XmlElement(name = "DIAG_RSLT")
    private Integer diagRslt;

    @XmlElement(name = "REC_RSLT")
    private Integer recRslt;

}
