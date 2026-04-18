package ru.orlov.medflk.calc.hospital.domain;

import lombok.Data;
import ru.orlov.medflk.jaxb.Sl;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.right;

/**
 * "Временная таблица" из приложения 8.
 * Класс для хранения промежуточных вычислений
 */

@Data
public class CalcData {
    private Integer nZap;
    private Sl sl;
    private Integer kd;
    private String nKsg;
    private BigDecimal koefZ;
    private BigDecimal sumKsg = BigDecimal.ZERO; // сумма по КСГ без прерывания
    private BigDecimal sumSubtotal = BigDecimal.ZERO; // с прерыванием
    private BigDecimal sumDial = BigDecimal.ZERO; // сумма диализа по случаю
    private BigDecimal sumTotal = BigDecimal.ZERO; // принятая сумма по случаю
    private BigDecimal sumMo = BigDecimal.ZERO; // сумма от МО, для отладки

    private Integer priority = 0;
    private String priorityReason;
    private String exceptionalReason;
    private Set<String> interruptReasons = new HashSet<>();
    private Set<String> paymentReason = new HashSet<>();
    private GroupKsg gKsg;
    private Boolean selected = false;

    private final DateTimeFormatter dmy = DateTimeFormatter.ofPattern("dd.MM");

    public static String toStringHeader() {
        // KD - кол-во койко-дней
        // DS1 - диагноз
        // N_KSG - возможный КСГ, выбранный по базовому алгоритму группировщика
        // SUM_KSG - сумма по этому КСГ
        // PRIOR - приоритет и причина его установки из Приложения 8
        // EXC - причина особенного алгоритма расчёта из Приложения 9
        // PPR - причина прерванности случая оплаты по КСГ
        // SUBTOTAL - сумма по КСГ с учётом прерывания
        // M - флаг выбранного КСГ для случая
        // OPL - флаг оплаты по 1 или нескольким КСГ
        return "SL_ID|DATES      |KD|DS1|N_KSG   |  SUM_KSG|PRIOR|EXC|PPR| SUBTOTAL|M|OPL| SUM_DIAL|SUM_TOTAL|   SUM_MO";
    }

    @Override
    public String toString() {
        String slId = sl.getSlId();
        String slIdFmt = slId.length() >= 5 ? ".." + right(slId, 3) : leftPad(slId, 5);
        String datesFmt = sl.getDate1().format(dmy) + "-" + sl.getDate2().format(dmy);
        return """
                %s|%s|%s|%s|%s|%s|%s%s |%s |%s |%s|%s|%s|%s|%s|%s
                """.formatted(
                slIdFmt, datesFmt, String.format("%2d", kd),
                sl.getDs1().substring(0, 3), nKsg, fmtSum(sumKsg), String.format("%2d", priority),
                priorityReason == null ? "  " : "." + priorityReason,
                exceptionalReason == null ? "  " : leftPad(exceptionalReason, 2),
                leftPad(String.join("", interruptReasons), 2),
                fmtSum(sumSubtotal), selected ? "+" : " ",
                leftPad(String.join("", paymentReason), 2) + " ",
                fmtSum(sumDial), fmtSum(sumTotal), fmtSum(sumMo)
        ).replaceAll("\\n", "");
    }

    private String fmtSum(BigDecimal v) {
        return v.compareTo(BigDecimal.ZERO) > 0 ? String.format("%9.2f", v) : leftPad(" ", 9);
    }
}
