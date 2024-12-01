package ru.irkoms.medflk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.irkoms.medflk.domain.Q015Packet;
import ru.irkoms.medflk.domain.Q015Service;
import ru.irkoms.medflk.jaxb.FlkP;
import ru.irkoms.medflk.jaxb.meta.APersList;
import ru.irkoms.medflk.jaxb.meta.AZlList;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.right;

@Log4j2
@Service
@RequiredArgsConstructor
public class Q015ValidationService {

    private final Q015Service q015Service;

    public List<FlkP.Pr> validate(AZlList zlList, APersList persList) {
        String zlType = right(zlList.getClass().getSimpleName(), 1); // [CHTX]

        List<Q015Packet.Q015> q015List = new ArrayList<>();
        q015List.addAll(q015Service.getTestsForType(zlType));
        q015List.addAll(q015Service.getTestsForType("L"));

        return List.of();
    }

}
