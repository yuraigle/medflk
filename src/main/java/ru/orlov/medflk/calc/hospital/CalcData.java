package ru.orlov.medflk.calc.hospital;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CalcData {
    private Integer nZap;
    private String slId;
    private String nKsg;
    private BigDecimal koefZ;
    private BigDecimal sumKsg;
    private BigDecimal sumDial;

    private Integer priority;
    private String priorityReason;
    private String specialReason;
    private Set<String> interruptReasons;
    private Set<String> paymentReason;
}
