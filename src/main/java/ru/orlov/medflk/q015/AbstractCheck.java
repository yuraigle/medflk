package ru.orlov.medflk.q015;

import ru.orlov.medflk.jaxb.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCheck {

    public abstract String getErrorMessage();

    public abstract List<FlkErr> check(ZlList zlList, PersList persList);

    List<FlkErr> iterateOverZap(ZlList zlList, PersList persList, IFunctionOverZap func) {
        List<FlkErr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null) continue;

            List<FlkErr> zapErrors = func.apply(zlList, zap);
            if (zapErrors != null) {
                errors.addAll(zapErrors);
            }
        }

        return errors;
    }

    List<FlkErr> iterateOverSl(ZlList zlList, PersList persList, IFunctionOverSl func) {
        List<FlkErr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (Sl sl : zap.getZSl().getSlList()) {
                List<FlkErr> slErrors = func.apply(zlList, zap, sl);
                if (slErrors != null) {
                    errors.addAll(slErrors);
                }
            }
        }

        return errors;
    }

    List<FlkErr> iterateOverUsl(ZlList zlList, PersList persList, IFunctionOverUsl func) {
        List<FlkErr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (Sl sl : zap.getZSl().getSlList()) {
                if (sl.getUslList() == null) continue;

                for (Usl usl : sl.getUslList()) {
                    List<FlkErr> uslErrors = func.apply(zlList, zap, sl, usl);
                    if (uslErrors != null) {
                        errors.addAll(uslErrors);
                    }
                }
            }
        }

        return errors;
    }

    List<FlkErr> iterateOverOnkSl(ZlList zlList, PersList persList, IFunctionOverOnkSl func) {
        List<FlkErr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (Sl sl : zap.getZSl().getSlList()) {
                if (sl.getOnkSl() == null) continue;

                List<FlkErr> errors1 = func.apply(zlList, zap, sl, sl.getOnkSl());
                if (errors1 != null) {
                    errors.addAll(errors1);
                }
            }
        }

        return errors;
    }

    List<FlkErr> iterateOverOnkUsl(ZlList zlList, PersList persList, IFunctionOverOnkUsl func) {
        List<FlkErr> errors = new ArrayList<>();

        for (Zap zap : zlList.getZapList()) {
            if (zap.getZSl() == null || zap.getZSl().getSlList() == null) continue;

            for (Sl sl : zap.getZSl().getSlList()) {
                if (sl.getOnkSl() == null) continue;

                OnkSl onkSl = sl.getOnkSl();
                if (onkSl.getOnkUslList() == null) continue;

                for (OnkUsl onkUsl : onkSl.getOnkUslList()) {
                    List<FlkErr> errors1 = func.apply(zlList, zap, sl, onkSl, onkUsl);
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
        List<FlkErr> apply(ZlList zlList, Zap zap);
    }

    @FunctionalInterface
    interface IFunctionOverSl {
        List<FlkErr> apply(ZlList zlList, Zap zap, Sl sl);
    }

    @FunctionalInterface
    interface IFunctionOverUsl {
        List<FlkErr> apply(ZlList zlList, Zap zap, Sl sl, Usl usl);
    }

    @FunctionalInterface
    interface IFunctionOverOnkSl {
        List<FlkErr> apply(ZlList zlList, Zap zap, Sl sl, OnkSl onkSl);
    }

    @FunctionalInterface
    interface IFunctionOverOnkUsl {
        List<FlkErr> apply(ZlList zlList, Zap zap, Sl sl, OnkSl onkSl, OnkUsl onkUsl);
    }

}
