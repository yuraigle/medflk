package ru.irkoms.medflk.jaxb.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimal5Adapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public BigDecimal unmarshal(String v) throws Exception {
        return new BigDecimal(v);
    }

    @Override
    public String marshal(BigDecimal v) throws Exception {
        if (v != null) {
            return v.setScale(5, RoundingMode.HALF_UP).toString();
        }
        return null;
    }
}
