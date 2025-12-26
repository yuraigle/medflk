package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "PACKET")
@XmlAccessorType(XmlAccessType.NONE)
public class F002Packet {

    @XmlElement(name = "VERSION")
    private String version;

    @XmlElement(name = "DATE")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date;

    @XmlElement(name = "insCompany")
    private List<F002> zapList;

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class F002 {

        @XmlElement(name = "TF_OKATO")
        private String tfOkato;

        @XmlElement(name = "smocod")
        private String smocod;

        @XmlElement(name = "Ogrn")
        private String ogrn;

    }
}
