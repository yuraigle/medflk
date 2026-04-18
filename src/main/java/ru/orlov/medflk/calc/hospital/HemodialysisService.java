package ru.orlov.medflk.calc.hospital;

import org.springframework.stereotype.Service;
import ru.orlov.medflk.jaxb.Sl;
import ru.orlov.medflk.jaxb.Usl;

import java.math.BigDecimal;

@Service
public class HemodialysisService {

    // Можно просто соглашаться с суммой от МО, она проверяется на ФЛК
    public BigDecimal calcSumDial(Sl sl) {
        return sl.getUslList() == null ? BigDecimal.ZERO : sl.getUslList().stream()
                .filter(u -> u.getCodeUsl().startsWith("A18"))
                .map(Usl::getSumvUsl)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
