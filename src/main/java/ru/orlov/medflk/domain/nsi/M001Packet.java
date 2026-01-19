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
public class M001Packet {

    @XmlElement(name = "entries")
    private Entries entries;

    @Getter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Entries {

        @XmlElement(name = "entry")
        private List<M001> entryList;
    }

    @Getter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class M001 {

        @XmlElement(name = "ID")
        private Integer id;

        @XmlElement(name = "REC_CODE")
        private String recCode;

        @XmlElement(name = "MKB_CODE")
        private String mkbCode;

        @XmlElement(name = "MKB_NAME")
        private String mkbName;

        @XmlElement(name = "ID_PARENT")
        private Integer idParent;

        @XmlElement(name = "ACTUAL")
        private Integer actual;
    }

}
