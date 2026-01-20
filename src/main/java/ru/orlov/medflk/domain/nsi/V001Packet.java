package ru.orlov.medflk.domain.nsi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

import java.util.List;

@Getter
@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.NONE)
public class V001Packet {

    @XmlElement(name = "entries")
    private Entries entries;

    @Getter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Entries {

        @XmlElement(name = "entry")
        private List<V001> entryList;
    }

    @Getter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class V001 {

        @XmlElement(name = "code")
        private Integer code;

        @XmlElement(name = "data")
        private Data data;

        @Getter
        @XmlAccessorType(XmlAccessType.NONE)
        public static class Data {

            @XmlElement(name = "S_CODE")
            private String sCode;

            @XmlElement(name = "REL")
            private Integer rel;

            @XmlElement(name = "ID")
            private Integer id;

            @XmlElement(name = "NAME")
            private String name;
        }
    }
}
