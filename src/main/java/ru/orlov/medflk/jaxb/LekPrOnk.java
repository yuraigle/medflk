package ru.orlov.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.NONE)
public class LekPrOnk {

    @XmlElement(name = "REGNUM")
    private String regnum;

    @XmlElement(name = "REGNUM_DOP")
    private String regnumDop;

    @XmlElement(name = "CODE_SH")
    private String codeSh;

    @XmlElement(name = "INJ")
    private List<@Valid Inj> injList;

    @XmlElement(name = "DATE_INJ")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private List<LocalDate> dataInj;

}
