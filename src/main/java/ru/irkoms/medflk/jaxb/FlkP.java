package ru.irkoms.medflk.jaxb;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.ASl;
import ru.irkoms.medflk.jaxb.meta.AUsl;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@XmlRootElement(name = "FLK_P")
@XmlAccessorType(XmlAccessType.NONE)
public class FlkP {

    @XmlElement(name = "FNAME")
    private String fname;

    @XmlElement(name = "FNAME_I")
    private String fnameI;

    @XmlElement(name = "PR")
    private List<Pr> prList = new ArrayList<>();

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @XmlAccessorType(XmlAccessType.NONE)
    public static class Pr {

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

        @XmlTransient
        private String enp;

        @XmlTransient
        private String level;

        public Pr(AZap zap, ASl sl, Object value) {
            if (zap != null) {
                this.nZap = zap.getNZap().toString();

                if (zap.getZSl() != null) {
                    this.idcase = zap.getZSl().getIdcase().toString();
                }
                if (zap.getPacient() != null) {
                    this.idPac = zap.getPacient().getIdPac();
                    this.enp = zap.getPacient().getNpolis();
                }
            }

            if (sl != null) {
                this.slId = sl.getSlId();
            }

            this.znPol = value == null ? "" : value.toString();
        }

        public Pr(AZap zap, ASl sl, AUsl usl, Object value) {
            this(zap, sl, value);

            if (usl != null) {
                this.idserv = usl.getIdserv();
            }
        }

        public Pr(APers pers, Object value) {
            this.idPac = pers.getIdPac();
            this.znPol = value == null ? "" : value.toString();
        }
    }
}