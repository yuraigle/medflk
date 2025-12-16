package ru.orlov.medflk.service;

import jakarta.validation.*;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SchemaValidationService {

    private final Validator validator;

    public SchemaValidationService() {
        try (
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        ) {
            validator = factory.getValidator();
        }
    }

    public List<FlkP.Pr> validate(Object list) {
        Set<ConstraintViolation<Object>> err = validator.validate(list);

        List<FlkP.Pr> errors = new ArrayList<>();
        for (ConstraintViolation<Object> viol : err) {
            FlkP.Pr pr = convertHibernateViolation(viol);
            errors.add(pr);
        }

        return errors;
    }

    private FlkP.Pr convertHibernateViolation(ConstraintViolation<Object> viol) {
        FlkP.Pr pr = new FlkP.Pr();
        pr.setLevel("E"); // ошибки схемы XML являются критическими

        String imPol = null;
        ZlList aZlList = null;
        Zap aZap = null;
        Pacient aPacient = null;
        ZSl aZSl = null;

        Matcher m1 = Pattern.compile("^persList\\[([0-9]+)]\\..+$")
                .matcher(viol.getPropertyPath().toString());
        if (m1.matches()) {
            int pacN = Integer.parseInt(m1.group(1));
            PersList aPersList = (PersList) viol.getRootBean();
            PersList.Pers aPers = aPersList.getPersList().get(pacN);
            pr.setIdPac(aPers.getIdPac());
        }

        for (Path.Node node : viol.getPropertyPath()) {
            String n0 = node.getName();
            Integer i0 = node.getIndex();

            if (n0.equals("zapList")) {
                aZlList = (ZlList) viol.getRootBean();
            }

            if (n0.matches("zSl|pacient") && aZlList != null) {
                aZap = aZlList.getZapList().get(i0);
                aZSl = aZap.getZSl();
                aPacient = aZap.getPacient();
            }

            imPol = n0.toUpperCase();
        }

        if (aPacient != null) {
            pr.setIdPac(aPacient.getIdPac());
        }

        if (aZap != null && aZap.getNZap() != null) {
            pr.setNZap(aZap.getNZap().toString());
        }

        if (aZSl != null && aZSl.getIdcase() != null) {
            pr.setIdcase(aZSl.getIdcase().toString());
        }

        pr.setImPol(imPol);
        pr.setComment(imPol + " : " + viol.getMessage());

        if (viol.getInvalidValue() != null) {
            pr.setZnPol(viol.getInvalidValue().toString());
        }

        return pr;
    }
}
