package ru.irkoms.medflk.q015;

import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APers;
import ru.irkoms.medflk.jaxb.meta.AZap;

import java.util.List;

public abstract class AbstractCheckZapWithPers {

    public abstract List<FlkP.Pr> check(AZap zap, APers pers);

}
