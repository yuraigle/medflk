package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateRusAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "packet")
@XmlAccessorType(XmlAccessType.NONE)
public class N003Packet {

    @XmlElement(name = "zap")
    private List<N003> zapList;

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class N003 {

        @XmlElement(name = "ID_T")
        private Integer idT;

        @XmlElement(name = "DS_T")
        private String dsT;

        @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
        @XmlElement(name = "DATEBEG")
        private LocalDate datebeg;

        @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
        @XmlElement(name = "DATEEND")
        private LocalDate dateend;

    }
}
