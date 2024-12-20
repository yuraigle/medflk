package ru.irkoms.medflk.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import ru.irkoms.medflk.jaxb.util.LocalDateRusAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@XmlRootElement(name = "packet")
@XmlAccessorType(XmlAccessType.NONE)
public class F032Packet {

    @XmlElement(name = "zap")
    private List<F032> zapList;

    @Getter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class F032 {

        @XmlElement(name = "MCOD")
        private String mcod;

        @XmlElement(name = "OKTMO_P")
        private String oktmoP;

        @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
        @XmlElement(name = "DATEBEG")
        private LocalDate datebeg;

        @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
        @XmlElement(name = "DATEEND")
        private LocalDate dateend;

    }
}
