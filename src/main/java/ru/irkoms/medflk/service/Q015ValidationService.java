package ru.irkoms.medflk.service;

import org.springframework.stereotype.Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.List;

@Service
public class Q015ValidationService {

    public List<FlkP.Pr> validate(AZlList zlList, APersList persList) {
        return List.of();
    }

}
