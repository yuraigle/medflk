package ru.orlov.medflk.calc.hospital;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Дополнительная информация о КСГ из группировщика
 */

@Data
public class GroupKsgData {

    private String nKsg;
    private BigDecimal koefZ;
    private Integer profil;
    private BigDecimal dolZp;

}
