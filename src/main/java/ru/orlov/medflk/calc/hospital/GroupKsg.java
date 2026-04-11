package ru.orlov.medflk.calc.hospital;

import lombok.Data;

/**
 * Информация о КСГ из группировщика
 */

@Data
public class GroupKsg {

    private Integer n;
    private String ds1;
    private String ds2;
    private String ds3;
    private String codeUsl;
    private Integer age;
    private Integer sex;
    private Integer kd;
    private String dkk;
    private String frak;
    private String nKsg;
    private Integer ngr;

}
