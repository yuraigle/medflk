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
public class LekPr {

    @XmlElement(name = "DATA_INJ")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate dataInj;

    @XmlElement(name = "CODE_SH")
    private String codeSh;

    @XmlElement(name = "REGNUM")
    private String regnum;

    @XmlElement(name = "COD_MARK")
    private String codMark;

    @XmlElement(name = "LEK_DOSE")
    private LekDose lekDose;

}
