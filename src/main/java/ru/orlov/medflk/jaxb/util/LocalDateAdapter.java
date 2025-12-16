package ru.orlov.medflk.jaxb.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String marshal(LocalDate v) {
        synchronized (dateFormat) {
            return (v == null) ? null : dateFormat.format(v);
        }
    }

    @Override
    public LocalDate unmarshal(String v) {
        synchronized (dateFormat) {
            if (v.length() > 10) {
                v = v.substring(0, 10);
            }

            return LocalDate.parse(v, dateFormat);
        }
    }
}
