package ru.orlov.medflk.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static ru.orlov.medflk.Utils.rxFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@XmlRootElement(name = "FLK_P")
@XmlAccessorType(XmlAccessType.NONE)
public class FlkP {

    @XmlElement(name = "FNAME")
    private String fname;

    @XmlElement(name = "FNAME_I")
    private String fnameI;

    @XmlElement(name = "PR")
    private List<FlkErr> prList = new ArrayList<>();

    public FlkP(String fnameI) {
        this.fnameI = fnameI.replaceAll("\\.[a-zA-Z]{3}$", "");

        if (rxFile.matcher(fnameI.toUpperCase()).matches()) {
            this.fname = fnameI.toUpperCase().replaceAll("^.", "F");
        } else {
            this.fname = "FLK_" + fnameI;
        }

    }
}
