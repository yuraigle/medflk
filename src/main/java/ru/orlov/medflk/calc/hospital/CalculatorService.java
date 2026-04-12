package ru.orlov.medflk.calc.hospital;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.calc.hospital.domain.CalcData;
import ru.orlov.medflk.calc.hospital.domain.GroupKsg;
import ru.orlov.medflk.calc.hospital.domain.GroupKsgService;
import ru.orlov.medflk.domain.nsi.V023Packet;
import ru.orlov.medflk.domain.nsi.V023Service;
import ru.orlov.medflk.jaxb.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNullElse;

@Log4j2
@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final V023Service v023Service;
    private final GroupKsgService groupKsgService;
    private final KsgMathsService ksgMathsService;
    private final InterruptReasonsService interruptReasonsService;
    private final PriorityReasonsService priorityReasonsService;
    private final ExceptionalReasonsService exceptionalReasonsService;

    public void calcFile(ZlList zlList, PersList persList) {
        for (Zap zap : zlList.getZapList()) {
            Integer uslOk = zap.getZSl().getUslOk();
            boolean isHosp = uslOk != null && List.of(1, 2).contains(uslOk);

            if (isHosp && zap.getNZap() == 33) {
                Pers pers = persList.getPersList().stream()
                        .filter(p -> Objects.equals(p.getIdPac(), zap.getPacient().getIdPac()))
                        .findFirst().orElseThrow();

                calcZapHospital(zap, pers);
            }
        }
    }

    public void calcZapHospital(Zap zap, Pers pers) {
        Integer uslOk = zap.getZSl().getUslOk();
        List<CalcData> calcData = new ArrayList<>();

        for (Sl sl : zap.getZSl().getSlList()) {
            KsgKpg ksgKpg = sl.getKsgKpg();
            if (ksgKpg == null) {
                continue;
            }

            // Эти значения можно взять из реестра, но обязательно проверять на ФЛК
            BigDecimal basicRate = ksgKpg.getBztsz(); // БС - базовая ставка
            BigDecimal koefD = ksgKpg.getKoefD(); // КД - коэффициент дифференциации
            BigDecimal koefU = ksgKpg.getKoefU(); // КУС_МО - коэффициент уровня/подуровня МО

            // Это значение надо перепроверить за МО
            Integer kd = sl.getKd(); // кол-во койко-дней

            // 8.2.5 на третьем этапе осуществляется фильтрация основной таблицы "Группировщик"
            List<GroupKsg> ksgList = groupKsgService.findAllPossibleKsg(sl, pers, uslOk, kd);

            // и заполнение временной таблицы значениями.
            for (GroupKsg gKsg : ksgList) {
                V023Packet.V023 v023 = v023Service.getKsgOnDate(gKsg.getNKsg(), sl.getDate2());

                BigDecimal koefUp = ksgKpg.getKoefUp(); // КС_КСГ - коэффициент специфики // todo из базы
                BigDecimal dolZp = gKsg.getDZp() == null ? BigDecimal.ONE : gKsg.getDZp();
                List<KsgKpg.SlKoef> kslp = ksgKpg.getSlKoefList(); // коэффициенты сложности

                BigDecimal sumWithKsg = ksgMathsService
                        .calcSumSlWithKsg(v023, basicRate, koefD, koefU, koefUp, dolZp, kslp);

                CalcData c = new CalcData();
                c.setNZap(zap.getNZap());
                c.setSl(sl);
                c.setNKsg(gKsg.getNKsg());
                c.setKoefZ(v023.getKoefZ());
                c.setSumKsg(sumWithKsg);
                c.setGKsg(gKsg);

                calcData.add(c);
            }

            // проставляем флаги прерванности
            Set<String> possibleKsg = calcData.stream().map(CalcData::getNKsg).collect(Collectors.toSet());
            List<String> critList = requireNonNullElse(ksgKpg.getCritList(), Collections.emptyList());
            for (CalcData c : calcData) {
                Set<String> reasons = interruptReasonsService
                        .findInterruptReasons(zap.getZSl(), sl.getSlId(), kd, c.getNKsg(), possibleKsg, critList);
                c.getInterruptReasons().addAll(reasons);
            }

            // проставляем приоритеты
            for (CalcData c : calcData) {
                priorityReasonsService.checkPriority(c, possibleKsg, zap.getZSl().getRslt());
            }

            // проставляем исключительность
            for (CalcData c : calcData) {
                String reason = exceptionalReasonsService
                        .findExceptionalReason(zap.getZSl(), sl.getSlId(), c.getNKsg());
                c.setExceptionalReason(reason);
            }
        }

        log.info(CalcData.toStringHeader());
        calcData.forEach(log::info);
    }

}
