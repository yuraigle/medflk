package ru.orlov.medflk.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "FLK_P")
@XmlAccessorType(XmlAccessType.NONE)
public class FlkP {

    @XmlElement(name = "FNAME")
    private String fname;

    @XmlElement(name = "FNAME_I")
    private String fnameI;

    @XmlElement(name = "PR")
    private List<FlkErr> prList = new ArrayList<>();

}
