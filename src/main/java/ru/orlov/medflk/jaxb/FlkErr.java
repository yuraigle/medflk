package ru.orlov.medflk.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import ru.orlov.medflk.domain.nsi.Q015Packet;
import ru.orlov.medflk.q015.AbstractCheck;

import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.NONE)
public class FlkErr {

    @XmlElement(name = "OSHIB")
    private String oshib;

    @XmlElement(name = "IM_POL")
    private String imPol;

    @XmlElement(name = "ZN_POL")
    private String znPol;

    @XmlElement(name = "BAS_EL")
    private String basEl;

    @XmlElement(name = "N_ZAP")
    private String nZap;

    @XmlElement(name = "ID_PAC")
    private String idPac;

    @XmlElement(name = "IDCASE")
    private String idcase;

    @XmlElement(name = "SL_ID")
    private String slId;

    @XmlElement(name = "IDSERV")
    private String idserv;

    @XmlElement(name = "COMMENT")
    private String comment;

    public FlkErr(Zap zap, Sl sl, Usl usl, Object value) {
        if (zap != null) {
            this.nZap = zap.getNZap().toString();

            if (zap.getZSl() != null) {
                this.idcase = zap.getZSl().getIdcase().toString();
            }

            if (zap.getPacient() != null) {
                this.idPac = zap.getPacient().getIdPac();
            }
        }

        if (sl != null) {
            this.slId = sl.getSlId();
        }

        if (usl != null) {
            this.idserv = usl.getIdserv();
        }

        this.znPol = value == null ? "" : value.toString();
    }

    public FlkErr(Pers pers, Object value) {
        this.idPac = pers.getIdPac();
        this.znPol = value == null ? "" : value.toString();
    }

    public void fillFromQ015(Q015Packet.Q015 q015) {
        this.oshib = q015.getIdTest();

        // ZL_LIST/ZAP/Z_SL/SL/ONK_SL => (SL, ONK_SL)
        if (q015.getIdEl() != null && q015.getIdEl().contains("/")) {
            List<String> parts = List.of(q015.getIdEl().split("/"));
            this.imPol = parts.getLast();
            if (parts.size() > 1) {
                this.basEl = parts.get(parts.size() - 2);
            }
        }

        Object bean = q015.getBean();
        if (bean instanceof AbstractCheck) {
            this.comment = ((AbstractCheck) bean).getErrorMessage();
        }
    }
}
