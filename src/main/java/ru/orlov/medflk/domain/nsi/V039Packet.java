package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import ru.orlov.medflk.jaxb.util.LocalDateRusAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@XmlRootElement(name = "packet")
@XmlAccessorType(XmlAccessType.NONE)
public class V039Packet {

    @XmlElement(name = "zglv")
    private NsiDefaultZglv zglv;

    @XmlElement(name = "zap")
    private List<V039> zapList;

    @Getter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class V039 {

        @XmlElement(name = "ID_VZ")
        private Integer idVz;

        @XmlElement(name = "N_VZ")
        private String nVz;

        @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
        @XmlElement(name = "DATEBEG")
        private LocalDate datebeg;

        @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
        @XmlElement(name = "DATEEND")
        private LocalDate dateend;

    }
}
