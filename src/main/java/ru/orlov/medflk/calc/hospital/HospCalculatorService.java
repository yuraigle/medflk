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

/**
 * Класс реализует расчёт стоимости случаев оказания медицинской помощи
 * в стационаре, согласно Методическим Рекомендациям от 2026 года
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class HospCalculatorService {

    private final V023Service v023Service;
    private final GroupKsgService groupKsgService;
    private final KsgMathsService ksgMathsService;
    private final InterruptReasonsService interruptReasonsService;
    private final PriorityReasonsService priorityReasonsService;
    private final ExceptionalReasonsService exceptionalReasonsService;
    private final MultiKsgReasonsService multiKsgReasonsService;

    public void calcFile(ZlList zlList, PersList persList) {
        Map<String, Pers> persMap = new HashMap<>();
        persList.getPersList().forEach(p -> persMap.put(p.getIdPac(), p));

        for (Zap zap : zlList.getZapList()) {
            Integer uslOk = zap.getZSl().getUslOk();
            boolean isHosp = uslOk != null && List.of(1, 2).contains(uslOk);
            if (!isHosp) continue;

            Pers pers = persMap.get(zap.getPacient().getIdPac());
            if (zap.getNZap() == 33) {
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

            List<CalcData> calcData1 = new ArrayList<>();

            // Эти значения можно взять из реестра, но обязательно проверять на ФЛК
            BigDecimal basicRate = ksgKpg.getBztsz(); // БС - базовая ставка
            BigDecimal koefD = ksgKpg.getKoefD(); // КД - коэффициент дифференциации
            BigDecimal koefU = ksgKpg.getKoefU(); // КУС_МО - коэффициент уровня/подуровня МО

            BigDecimal sumDial = BigDecimal.ZERO; // todo: сумма диализа

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
                c.setSumDial(sumDial);
                c.setGKsg(gKsg);

                calcData1.add(c);
            }

            // проставляем флаги прерванности
            Set<String> possibleKsg = calcData1.stream().map(CalcData::getNKsg).collect(Collectors.toSet());
            List<String> critList = requireNonNullElse(ksgKpg.getCritList(), Collections.emptyList());
            for (CalcData c : calcData1) {
                Set<String> reasons = interruptReasonsService
                        .findInterruptReasons(zap.getZSl(), sl.getSlId(), kd, c.getNKsg(), possibleKsg, critList);
                c.getInterruptReasons().addAll(reasons);
            }

            // сумма с учётом прерванности
            Set<String> uslList = sl.getUslList() == null ? new HashSet<>() :
                    sl.getUslList().stream().map(Usl::getCodeUsl).collect(Collectors.toSet());
            for (CalcData c : calcData1) {
                boolean isInterrupted = !c.getInterruptReasons().isEmpty();
                boolean isSurgery = uslList.stream().anyMatch(u -> u.startsWith("A16"));
                boolean isTromb = uslList.stream().anyMatch(u -> u.equals("A25.30.036.001"));
                BigDecimal koef = !isInterrupted ? BigDecimal.ONE : findInterruptedKoef(kd, isSurgery || isTromb);
                c.setSumTotal(c.getSumKsg().multiply(koef).add(c.getSumDial()));
            }

            // проставляем приоритеты и исключительность
            for (CalcData c : calcData1) {
                priorityReasonsService.checkPriority(c, possibleKsg, zap.getZSl().getRslt());

                String reason = exceptionalReasonsService
                        .findExceptionalReason(zap.getZSl(), sl.getSlId(), c.getNKsg());
                c.setExceptionalReason(reason);
            }

            // выбираем КСГ для случая
            boolean isExceptional = calcData1.stream()
                    .anyMatch(c -> c.getExceptionalReason() != null);
            calcData1.stream()
                    // если особый случай, выбираем только среди исключительных КСГ
                    .filter(c -> !isExceptional || c.getExceptionalReason() != null)
                    // по приоритету, затем по стоимости
                    .min(Comparator.comparing(CalcData::getPriority).reversed()
                            .thenComparing(Comparator.comparing(CalcData::getSumKsg).reversed())
                    )
                    .ifPresent(c -> c.setSelected(true));

            calcData.addAll(calcData1);
        }

        // проставляем возможность оплаты по двум и более КСГ

        // По умолчанию только случай с максимальной суммой
        boolean hasMultiKsg = calcData.stream()
                .anyMatch(c -> c.getSelected() && !c.getPaymentReason().isEmpty());
        if (!hasMultiKsg) {
            calcData.stream().filter(CalcData::getSelected)
                    .max(Comparator.comparing(CalcData::getSumKsg))
                    .ifPresent(c -> c.getPaymentReason().add("1"));
        }

        log.info(CalcData.toStringHeader());
        calcData.forEach(log::info);
    }

    // коэффициент принимается регионально
    private BigDecimal findInterruptedKoef(Integer kd, Boolean isHir) {
        if (isHir) {
            return BigDecimal.valueOf(kd <= 3 ? 0.8 : 1);
        } else {
            return BigDecimal.valueOf(kd <= 3 ? 0.5 : 0.8);
        }
    }
}
