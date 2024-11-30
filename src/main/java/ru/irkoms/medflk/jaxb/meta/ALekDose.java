package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ALekDose {
    private String edIzm;
    private BigDecimal doseInj;
    private String methodInj;
    private Integer colInj;

    public boolean isEmpty() {
        if (edIzm != null && !edIzm.trim().equals("0") && edIzm.trim().equals("")) {
            return false;
        }

        if (methodInj != null && !methodInj.trim().equals("0") && methodInj.trim().equals("")) {
            return false;
        }

        if (colInj != null && colInj > 0) {
            return false;
        }

        if (doseInj != null && doseInj.compareTo(BigDecimal.ZERO) > 0) {
            return false;
        }

        return true;
    }
}
