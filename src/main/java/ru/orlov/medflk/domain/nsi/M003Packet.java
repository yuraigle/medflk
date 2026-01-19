package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.NONE)
public class M003Packet {

    @XmlElement(name = "entries")
    private Entries entries;

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Entries {

        @XmlElement(name = "entry")
        private List<M003> entryList;
    }

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class M003 {

        @XmlElement(name = "ID")
        private Integer id;

        @XmlElement(name = "PROFILE")
        private String profile;

    }

}
