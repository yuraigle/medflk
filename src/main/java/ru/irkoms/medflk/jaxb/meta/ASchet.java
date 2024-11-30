package ru.irkoms.medflk.jaxb.meta;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ASchet {
    private Integer code;
    private String codeMo;
    private Integer year;
    private Integer month;
    private String nschet;
    private LocalDate dschet;
    private String plat;
    private BigDecimal summav;
    private String coments;
    private BigDecimal summap;
    private BigDecimal sankMek;
    private BigDecimal sankMee;
    private BigDecimal sankEkmp;
    private String disp;

    public abstract void setCode(Integer code);

    public abstract void setCodeMo(String codeMo);

    public abstract void setPlat(String plat);

    public abstract void setYear(Integer year);

    public abstract void setMonth(Integer month);

    public abstract void setNschet(String nschet);

    public abstract void setDschet(LocalDate dschet);

    public abstract void setSummap(BigDecimal summap);

    public abstract void setSankMek(BigDecimal sankMek);
}
