package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

import java.util.List;

@Getter
@XmlRootElement(name = "packet")
@XmlAccessorType(XmlAccessType.NONE)
public class O002Packet {

    @XmlElement(name = "zap")
    private List<O002> zapList;

    @Getter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class O002 {

        @XmlElement(name = "TER")
        private String ter;

        @XmlElement(name = "KOD1")
        private String kod1;

        @XmlElement(name = "KOD2")
        private String kod2;

        @XmlElement(name = "KOD3")
        private String kod3;

    }
}
