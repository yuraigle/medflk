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
    private String nKsg;
    private BigDecimal koefZ;
    private BigDecimal sumKsg;
    private BigDecimal sumDial;

    private Integer priority = 0;
    private String priorityReason;
    private String exceptionalReason;
    private Set<String> interruptReasons = new HashSet<>();
    private Set<String> paymentReason = new HashSet<>();
    private GroupKsg gKsg;
    private boolean selected;

    private final DateTimeFormatter dmy = DateTimeFormatter.ofPattern("dd.MM.yy");

    public static String toStringHeader() {
        // DS - группа диагноза (1-й символ)
        // N_KSG - возможный КСГ, выбранный по базовому алгоритму группировщика
        // KF_Z - коэффициент затратоёмкости из V023
        // SUM_KSG - сумма случая с прерыванием, если он будет рассчитан по этому КСГ
        // PRIOR - приоритет и причина его установки из Приложения 8
        // EXC - причина особенного алгоритма расчёта из Приложения 9
        // PR - причина прерванности случая оплаты по КСГ
        // SEL - флаг выбранного КСГ для случая
        // K2 - флаг оплаты по 1 или нескольким КСГ
        return "SL_ID|DATES            |DS|N_KSG   |KF_Z|  SUM_KSG|PRIOR|EXC|PR|SEL|K2";
    }

    @Override
    public String toString() {
        String slId = sl.getSlId();
        String slIdFmt = slId.length() >= 5 ? ".." + right(slId, 3) : leftPad(slId, 5);
        String datesFmt = sl.getDate1().format(dmy) + "-" + sl.getDate2().format(dmy);
        return """
                %s|%s| %s|%s|%s|%s|%s%s |%s |%s| %s |%s
                """.formatted(
                slIdFmt, datesFmt, sl.getDs1().substring(0, 1), nKsg, koefZ,
                String.format("%9.2f", sumKsg), String.format("%2d", priority),
                priorityReason == null ? "  " : "." + priorityReason,
                exceptionalReason == null ? "  " : leftPad(exceptionalReason, 2),
                leftPad(String.join("", interruptReasons), 2),
                selected ? "1" : " ",
                leftPad(String.join("", paymentReason), 2)
        ).replaceAll("\\n", "");
    }
}
