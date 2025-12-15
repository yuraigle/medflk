package ru.irkoms.medflk.q015;

import ru.irkoms.medflk.jaxb.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCheck {

    public abstract String getErrorMessage();

    public abstract List<FlkP.Pr> check(ZlList zlList, PersList persList);

    List<FlkP.Pr> iterateOverZap(ZlList zlList, PersList persList, IFunctionOverZap func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null) continue;

            List<FlkP.Pr> zapErrors = func.apply(zlList, zap);
            if (zapErrors != null) {
                errors.addAll(zapErrors);
            }
        }

        return errors;
    }

    List<FlkP.Pr> iterateOverSl(ZlList zlList, PersList persList, IFunctionOverSl func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (Sl sl : zap.getZSl().getSlList()) {
                List<FlkP.Pr> slErrors = func.apply(zlList, zap, sl);
                if (slErrors != null) {
                    errors.addAll(slErrors);
                }
            }
        }

        return errors;
    }

    List<FlkP.Pr> iterateOverUsl(ZlList zlList, PersList persList, IFunctionOverUsl func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (Sl sl : zap.getZSl().getSlList()) {
                if (sl.getUslList() == null) continue;

                for (Usl usl : sl.getUslList()) {
                    List<FlkP.Pr> uslErrors = func.apply(zlList, zap, sl, usl);
                    if (uslErrors != null) {
                        errors.addAll(uslErrors);
                    }
                }
            }
        }

        return errors;
    }

    List<FlkP.Pr> iterateOverOnkSl(ZlList zlList, PersList persList, IFunctionOverOnkSl func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (Sl sl : zap.getZSl().getSlList()) {
                if (sl.getOnkSl() == null) continue;

                List<FlkP.Pr> errors1 = func.apply(zlList, zap, sl, sl.getOnkSl());
                if (errors1 != null) {
                    errors.addAll(errors1);
                }
            }
        }

        return errors;
    }

    List<FlkP.Pr> iterateOverOnkUsl(ZlList zlList, PersList persList, IFunctionOverOnkUsl func) {
        List<FlkP.Pr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (Sl sl : zap.getZSl().getSlList()) {
                if (sl.getOnkSl() == null) continue;

                OnkSl onkSl = sl.getOnkSl();
                if (onkSl.getOnkUslList() == null) continue;

                for (OnkUsl onkUsl : onkSl.getOnkUslList()) {
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
        List<FlkP.Pr> apply(ZlList zlList, Zap zap);
    }

    @FunctionalInterface
    interface IFunctionOverSl {
        List<FlkP.Pr> apply(ZlList zlList, Zap zap, Sl sl);
    }

    @FunctionalInterface
    interface IFunctionOverUsl {
        List<FlkP.Pr> apply(ZlList zlList, Zap zap, Sl sl, Usl usl);
    }

    @FunctionalInterface
    interface IFunctionOverOnkSl {
        List<FlkP.Pr> apply(ZlList zlList, Zap zap, Sl sl, OnkSl onkSl);
    }

    @FunctionalInterface
    interface IFunctionOverOnkUsl {
        List<FlkP.Pr> apply(ZlList zlList, Zap zap, Sl sl, OnkSl onkSl, OnkUsl onkUsl);
    }

}
