package ru.irkoms.medflk.jaxb;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.irkoms.medflk.jaxb.util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@XmlRootElement(name = "PERS_LIST")
@XmlAccessorType(XmlAccessType.NONE)
public class PersList {

    @Valid
    @NotNull
    @XmlElement(name = "ZGLV")
    private Zglv zglv;

    @NotEmpty
    @XmlElement(name = "PERS")
    private List<@Valid Pers> persList;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Zglv {

        @NotNull
        @Size(min = 3, max = 5)
        @XmlElement(name = "VERSION")
        private String version;

        @XmlElement(name = "DATA")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate data;

        @NotNull
        @Pattern(regexp = "^[A-Za-z0-9_]{13,26}$")
        @XmlElement(name = "FILENAME")
        private String filename;

        @NotNull
        @Pattern(regexp = "^[A-Za-z0-9_]{13,26}$")
        @XmlElement(name = "FILENAME1")
        private String filename1;
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Pers {

        @NotBlank
        @Size(min = 1, max = 36)
        @XmlElement(name = "ID_PAC")
        private String idPac;

        @Size(max = 40)
        @XmlElement(name = "FAM")
        private String fam;

        @Size(max = 40)
        @XmlElement(name = "IM")
        private String im;

        @Size(max = 40)
        @XmlElement(name = "OT")
        private String ot;

        @Min(1)
        @Max(2)
        @NotNull
        @XmlElement(name = "W")
        private Integer w;

        @NotNull
        @XmlElement(name = "DR")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate dr;

        @XmlElement(name = "DOST")
        private List<Integer> dostList;

        // @Pattern(regexp = "^[0-9А-ЯЁа-яё \\-]{0,100}$", message = "10-100 цифр и букв")
        @XmlElement(name = "TEL")
        private String tel;

        @Size(max = 40)
        @XmlElement(name = "FAM_P")
        private String famP;

        @Size(max = 40)
        @XmlElement(name = "IM_P")
        private String imP;

        @Size(max = 40)
        @XmlElement(name = "OT_P")
        private String otP;

        @Min(1)
        @Max(2)
        @XmlElement(name = "W_P")
        private Integer wP;

        @XmlElement(name = "DR_P")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate drP;

        @XmlElement(name = "DOST_P")
        private List<Integer> dostPList;

        @Size(max = 100)
        @XmlElement(name = "MR")
        private String mr;

        @Max(40)
        @XmlElement(name = "DOCTYPE")
        private Integer docType;

        @Size(max = 10)
        @XmlElement(name = "DOCSER")
        private String docSer;

        @Size(max = 20)
        @XmlElement(name = "DOCNUM")
        private String docNum;

        @XmlElement(name = "DOCDATE")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate docDate;

        @Size(max = 1000)
        @XmlElement(name = "DOCORG")
        private String docOrg;

        @Size(max = 14)
        @Pattern(regexp = "^(|\\d{11}|\\d{3}-\\d{3}-\\d{3} \\d{2})$", message = "требуется 11 цифр, можно с разделителями")
        @XmlElement(name = "SNILS")
        private String snils;

        @Pattern(regexp = "^[0-9]{5,11}$", message = "требуется 11 цифр")
        @XmlElement(name = "OKATOG")
        private String okatoG;

        @Pattern(regexp = "^[0-9]{5,11}$", message = "требуется 11 цифр")
        @XmlElement(name = "OKATOP")
        private String okatoP;

        @Size(max = 250)
        @XmlElement(name = "COMENTP")
        private String comentp;
    }
}