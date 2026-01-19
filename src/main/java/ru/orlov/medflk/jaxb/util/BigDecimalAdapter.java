package ru.orlov.medflk.jaxb.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public BigDecimal unmarshal(String v) {
        return new BigDecimal(v);
    }

    @Override
    public String marshal(BigDecimal v) {
        if (v != null) {
            return v.setScale(2, RoundingMode.HALF_UP).toString();
        }
        return null;
    }
}
