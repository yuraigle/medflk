package ru.orlov.medflk.domain;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.jaxb.util.LocalDateRusAdapter;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

@Getter
@XmlRootElement(name = "packet")
@XmlAccessorType(XmlAccessType.NONE)
public class Q015Packet {

    @XmlElement(name = "zap")
    private List<Q015> zapList;

    @Getter
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Q015 {

        @XmlElement(name = "ID_TEST")
        private String idTest;

        @XmlElement(name = "ID_EL")
        private String idEl;

        @XmlElement(name = "TYPE_MD")
        private TypeMd typeMd;

        @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
        @XmlElement(name = "DATEBEG")
        private LocalDate datebeg;

        @XmlJavaTypeAdapter(LocalDateRusAdapter.class)
        @XmlElement(name = "DATEEND")
        private LocalDate dateend;

        @Getter
        @XmlAccessorType(XmlAccessType.NONE)
        public static class TypeMd {

            @XmlElement(name = "TYPE_D")
            private List<String> typeD;
        }

        @Setter
        @XmlTransient
        private Object bean;

        @Setter
        @XmlTransient
        private Method method;
    }

}
