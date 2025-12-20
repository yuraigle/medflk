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
public class M002Packet {

    @XmlElement(name = "entries")
    private Entries entries;

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Entries {

        @XmlElement(name = "entry")
        private List<M002> entryList;
    }

    @Getter
    @Setter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class M002 {

        @XmlElement(name = "ICD10")
        private String icd10;

        @XmlElement(name = "ICDOTopography")
        private String icdo;

        @XmlElement(name = "TNM_7")
        private String tnm7;

        @XmlElement(name = "TNM_8")
        private String tnm8;

    }

}
