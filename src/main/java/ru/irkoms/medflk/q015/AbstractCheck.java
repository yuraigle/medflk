package ru.irkoms.medflk.q015;

import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCheck {

    public abstract List<FlkP.Pr> check(AZlList zlList, APersList persList);

    List<FlkP.Pr> iterateOverZap(AZlList zlList, APersList persList, IFunctionOverZap func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (AZap zap : zlList.getZapList()) {
            if (zap.getZSl() == null) continue;

            List<FlkP.Pr> zapErrors = func.apply(zlList, zap);
            if (zapErrors != null) {
                errors.addAll(zapErrors);
            }
        }

        return errors;
    }

    List<FlkP.Pr> iterateOverSl(AZlList zlList, APersList persList, IFunctionOverSl func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (AZap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (ASl sl : zap.getZSl().getSlList()) {
                List<FlkP.Pr> slErrors = func.apply(zlList, zap, sl);
                if (slErrors != null) {
                    errors.addAll(slErrors);
                }
            }
        }

        return errors;
    }

    List<FlkP.Pr> iterateOverUsl(AZlList zlList, APersList persList, IFunctionOverUsl func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (AZap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (ASl sl : zap.getZSl().getSlList()) {
                if (sl.getUslList() == null) continue;

                for (AUsl usl : sl.getUslList()) {
                    List<FlkP.Pr> uslErrors = func.apply(zlList, zap, sl, usl);
                    if (uslErrors != null) {
                        errors.addAll(uslErrors);
                    }
                }
            }
        }

        return errors;
    }

    List<FlkP.Pr> iterateOverOnkSl(AZlList zlList, APersList persList, IFunctionOverOnkSl func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (AZap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (ASl sl : zap.getZSl().getSlList()) {
                if (sl.getOnkSl() == null) continue;

                List<FlkP.Pr> errors1 = func.apply(zlList, zap, sl, sl.getOnkSl());
                if (errors1 != null) {
                    errors.addAll(errors1);
                }
            }
        }

        return errors;
    }

    List<FlkP.Pr> iterateOverOnkUsl(AZlList zlList, APersList persList, IFunctionOverOnkUsl func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (AZap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (ASl sl : zap.getZSl().getSlList()) {
                if (sl.getOnkSl() == null) continue;

                AOnkSl onkSl = sl.getOnkSl();
                if (onkSl.getOnkUslList() == null) continue;

                for (AOnkUsl onkUsl : onkSl.getOnkUslList()) {
                    List<FlkP.Pr> errors1 = func.apply(zlList, zap, sl, onkSl, onkUsl);
                    if (errors1 != null) {
                        errors.addAll(errors1);
                    }
                }
            }
        }

        return errors;
    }

    @FunctionalInterface
    interface IFunctionOverZap {
        List<FlkP.Pr> apply(AZlList zlList, AZap zap);
    }

    @FunctionalInterface
    interface IFunctionOverSl {
        List<FlkP.Pr> apply(AZlList zlList, AZap zap, ASl sl);
    }

    @FunctionalInterface
    interface IFunctionOverUsl {
        List<FlkP.Pr> apply(AZlList zlList, AZap zap, ASl sl, AUsl usl);
    }

    @FunctionalInterface
    interface IFunctionOverOnkSl {
        List<FlkP.Pr> apply(AZlList zlList, AZap zap, ASl sl, AOnkSl onkSl);
    }

    @FunctionalInterface
    interface IFunctionOverOnkUsl {
        List<FlkP.Pr> apply(AZlList zlList, AZap zap, ASl sl, AOnkSl onkSl, AOnkUsl onkUsl);
    }

}
