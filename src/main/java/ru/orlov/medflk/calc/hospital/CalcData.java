package ru.orlov.medflk.calc.hospital;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.right;

@Data
public class CalcData {
    private Integer nZap;
    private String slId;
    private String nKsg;
    private BigDecimal koefZ;
    private BigDecimal sumKsg;
    private BigDecimal sumDial;

    private Integer priority = 0;
    private String priorityReason;
    private String specialReason;
    private Set<String> interruptReasons = new HashSet<>();
    private Set<String> paymentReason = new HashSet<>();

    @Override
    public String toString() {
        return """
                SL_ID=%s | N_KSG=%s | KZ=%s | SUM1=%s | PRIOR=%s%s | SPEC=%s | PR=%s | M_KSG=%s
                """.formatted(
                slId.length() >= 5 ? ".." + right(slId, 3) : leftPad(slId, 5),
                nKsg, koefZ, String.format("%9.2f", sumKsg), String.format("%2d", priority),
                priorityReason == null ? "  " : "." + priorityReason,
                specialReason == null ? "  " : leftPad(specialReason, 2),
                leftPad(String.join("", interruptReasons), 2),
                leftPad(String.join("", paymentReason), 2)
        ).trim().replaceAll("\\n", "");
    }
}
