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

    public List<FlkErr> validate(Object list) {
        Set<ConstraintViolation<Object>> err = validator.validate(list);

        List<FlkErr> errors = new ArrayList<>();
        for (ConstraintViolation<Object> viol : err) {
            FlkErr pr = convertHibernateViolation(viol);
            errors.add(pr);
        }

        return errors;
    }

    private FlkErr convertHibernateViolation(ConstraintViolation<Object> viol) {
        FlkErr err = new FlkErr(null, null, null, null);

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
            err.setIdPac(aPers.getIdPac());
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
            err.setIdPac(aPacient.getIdPac());
        }

        if (aZap != null && aZap.getNZap() != null) {
            err.setNZap(aZap.getNZap().toString());
        }

        if (aZSl != null && aZSl.getIdcase() != null) {
            err.setIdcase(aZSl.getIdcase().toString());
        }

        err.setImPol(imPol);
        err.setComment(imPol + " : " + viol.getMessage());

        if (viol.getInvalidValue() != null) {
            err.setZnPol(viol.getInvalidValue().toString());
        }

        return err;
    }
}
