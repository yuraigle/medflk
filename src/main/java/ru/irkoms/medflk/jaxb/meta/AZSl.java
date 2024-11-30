package ru.irkoms.medflk.jaxb.meta;

import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;
import ru.irkoms.medflk.jaxb.Sank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AZSl {
    private Integer idcase;
    private Integer uslOk;
    private Integer vidpom;
    private Integer forPom;
    private String nprMo;
    private LocalDate nprDate;
    private String lpu;
    private Integer vbr;
    private LocalDate dateZ1;
    private LocalDate dateZ2;
    private Integer pOtk;
    private Integer kdZ;
    private List<Integer> vnovMList;
    private Integer rslt;
    private Integer rsltD;
    private Integer ishod;
    private List<Integer> osSluchList;
    private Integer vbP;
    private Integer idsp;
    private BigDecimal sumv;
    private Integer oplata;
    private BigDecimal sump;
    private BigDecimal sankIt;

    private List<? extends ASl> slList;

    private List<Sank> sankList;

    public abstract void setSankList(List<@Valid Sank> sankList);

    public abstract void setSankIt(BigDecimal sankIt);

    public abstract void setSump(BigDecimal sump);
}
