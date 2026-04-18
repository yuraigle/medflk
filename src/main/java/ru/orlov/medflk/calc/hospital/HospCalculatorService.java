package ru.orlov.medflk.calc.hospital;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.calc.hospital.domain.CalcData;
import ru.orlov.medflk.calc.hospital.domain.GroupKsg;
import ru.orlov.medflk.calc.hospital.domain.GroupKsgService;
import ru.orlov.medflk.calc.hospital.domain.KsgSpecificRepo;
import ru.orlov.medflk.domain.nsi.V023Packet;
import ru.orlov.medflk.domain.nsi.V023Service;
import ru.orlov.medflk.jaxb.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private final KsgSpecificRepo ksgSpecificRepo;
    private final HemodialysisService hemodialysisService;

    public void calcFile(ZlList zlList, PersList persList) {
        Map<String, Pers> persMap = new HashMap<>();
        persList.getPersList().forEach(p -> persMap.put(p.getIdPac(), p));

        int i = 0;
        for (Zap zap : zlList.getZapList().stream().sorted(Comparator.comparing(Zap::getNZap)).toList()) {
            Integer uslOk = zap.getZSl().getUslOk();
            boolean isHosp = uslOk != null && List.of(1, 2).contains(uslOk);
            if (!isHosp) continue;

            Pers pers = persMap.get(zap.getPacient().getIdPac());
            List<CalcData> calcData = calcZapHospital(zap, pers);

            boolean isCorrect = calcData.stream()
                    .filter(CalcData::getSelected)
                    .allMatch(c -> {
                        if (!c.getPaymentReason().isEmpty()) {
                            return c.getSumMo().compareTo(c.getSumTotal()) == 0;
                        } else {
                            return c.getSumMo().compareTo(c.getSumDial()) == 0;
                        }
                    });

            List<Integer> excluded =  List.of(104);

            if (!isCorrect && !excluded.contains(zap.getNZap())) {
                log.info("N_ZAP={}", zap.getNZap());
                log.info(CalcData.toStringHeader());
                calcData.forEach(log::info);
                break;
            }

            i++;
        }

        log.info("{} passed", i);
    }

    public List<CalcData> calcZapHospital(Zap zap, Pers pers) {
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

            // Это значение надо перепроверить за МО
            Integer kd = calculateKd(sl, zap.getZSl().getUslOk()); // кол-во койко-дней

            // 8.2.5 на третьем этапе осуществляется фильтрация основной таблицы "Группировщик"
            List<GroupKsg> ksgList = groupKsgService.findAllPossibleKsg(sl, pers, uslOk, kd);

            // и заполнение временной таблицы значениями.
            for (GroupKsg gKsg : ksgList) {
                V023Packet.V023 v023 = v023Service.getKsgOnDate(gKsg.getNKsg(), sl.getDate2());

                BigDecimal koefUp = ksgSpecificRepo.getKsgKs(gKsg.getNKsg());
                BigDecimal dolZp = gKsg.getDZp() == null ? BigDecimal.ONE : gKsg.getDZp();
                List<KsgKpg.SlKoef> kslp = ksgKpg.getSlKoefList(); // коэффициенты сложности

                BigDecimal sumWithKsg = ksgMathsService
                        .calcSumSlWithKsg(v023, basicRate, koefD, koefU, koefUp, dolZp, kslp);

                CalcData c = new CalcData();
                c.setNZap(zap.getNZap());
                c.setSl(sl);
                c.setKd(kd);
                c.setNKsg(gKsg.getNKsg());
                c.setKoefZ(v023.getKoefZ());
                c.setSumKsg(sumWithKsg);
                c.setGKsg(gKsg);

                if (gKsg.getNKsg().equals(sl.getKsgKpg().getNKsg())) {
                    c.setSumMo(sl.getSumM());
                }

                calcData1.add(c);
            }

            // проставляем флаги прерванности
            Set<String> possibleKsg = calcData1.stream().map(CalcData::getNKsg).collect(Collectors.toSet());
            List<String> critList = requireNonNullElse(ksgKpg.getCritList(), Collections.emptyList());
            for (CalcData c : calcData1) {
                Set<String> reasons = interruptReasonsService
                        .findInterruptReasons(zap.getZSl(), sl.getSlId(), kd, c.getNKsg(), critList);
                c.getInterruptReasons().addAll(reasons);
            }

            // проставляем приоритеты и исключительность
            for (CalcData c : calcData1) {
                priorityReasonsService.checkPriority(c, possibleKsg, zap.getZSl().getRslt());

                String reason = exceptionalReasonsService
                        .findExceptionalReason(zap.getZSl(), sl.getSlId(), c.getNKsg());
                c.setExceptionalReason(reason);
            }

            calcData.addAll(calcData1);
        }

        // проставляем возможность оплаты по двум и более КСГ
        multiKsgReasonsService.fillMultiKsgReasons(calcData);

        // При оплате по 2 КСГ по основаниям 2-10 случай до перевода не считается прерванным по 2-4
        calcData.stream()
                .filter(c -> c.getPaymentReason() != null && !c.getPaymentReason().isEmpty())
                .filter(c -> c.getPaymentReason().stream().map(Integer::parseInt)
                        .anyMatch(i -> i >= 2 && i <= 10))
                .forEach(c -> c.getInterruptReasons().removeAll(Set.of("2", "3", "4")));

        // subtotal: сумма по КСГ с учётом прерванности
        for (CalcData c : calcData) {
            boolean isInterrupted = !c.getInterruptReasons().isEmpty();
            BigDecimal koef = !isInterrupted ? BigDecimal.ONE : findInterruptedKoef(c.getKd(), c.getNKsg());
            c.setSumSubtotal(c.getSumKsg().multiply(koef).setScale(2, RoundingMode.HALF_UP));
        }

        // выбираем КСГ для каждого случая
        for (Sl sl : zap.getZSl().getSlList()) {
            // выбираем КСГ для случая
            boolean isExceptional = calcData.stream()
                    .filter(c -> c.getSl().getSlId().equals(sl.getSlId()))
                    .anyMatch(c -> c.getExceptionalReason() != null);
            calcData.stream()
                    .filter(c -> c.getSl().getSlId().equals(sl.getSlId()))
                    // если особый случай, выбираем только среди исключительных КСГ
                    .filter(c -> !isExceptional || c.getExceptionalReason() != null)
                    // по приоритету, затем по стоимости
                    .min(Comparator.comparing(CalcData::getPriority).reversed()
                            .thenComparing(Comparator.comparing(CalcData::getSumSubtotal).reversed())
                    )
                    .ifPresent(c -> c.setSelected(true));
        }

        // добавляем сумму диализа
        calcData.stream().filter(CalcData::getSelected)
                .forEach(c -> c.setSumDial(hemodialysisService.calcSumDial(c.getSl())));

        // 0. По умолчанию только случай с максимальной суммой
        boolean hasMultiKsg = calcData.stream()
                .anyMatch(c -> c.getSelected() && !c.getPaymentReason().isEmpty());
        if (!hasMultiKsg) {
            calcData.stream().filter(CalcData::getSelected)
                    .max(Comparator.comparing(CalcData::getSumSubtotal))
                    .ifPresent(c -> c.getPaymentReason().add("0"));
        }

        // Если МО подала сумму 0.00 по ЗСЛ, соглашаемся (?)
        if (zap.getZSl().getSumv().compareTo(BigDecimal.ZERO) == 0) {
            calcData.forEach(c -> c.getPaymentReason().clear());
        }

        calcData.forEach(c -> {
            if (c.getSelected()) {
                BigDecimal total = BigDecimal.ZERO;
                if (!c.getPaymentReason().isEmpty()) {
                    total = total.add(c.getSumSubtotal());
                }
                total = total.add(c.getSumDial());
                c.setSumTotal(total);
            }
        });

        return calcData;
    }

    // коэффициент принимается регионально
    private BigDecimal findInterruptedKoef(Integer kd, String nKsg) {
        if (ksgSpecificRepo.isSurgeryOrTromb(nKsg)) {
            return BigDecimal.valueOf(kd <= 3 ? 0.8 : 1);
        } else {
            return BigDecimal.valueOf(kd <= 3 ? 0.5 : 0.8);
        }
    }

    private int calculateKd(Sl sl, Integer uslOk) {
        LocalDate d1 = sl.getDate1();
        LocalDate d2 = sl.getDate2();

        if (uslOk == 1) { // КС
            return (int) ChronoUnit.DAYS.between(d1, d2); // день выписки считаются за один
        } else { // ДС
            // День поступления и день выписки считаются за два.
            // Есть график работы в субботу, воскресенье и праздники.
            // Будем просто доверять информации от МО.

            int kdMax = (int) ChronoUnit.DAYS.between(d1, d2) + 1; // дней в периоде
            int kdMo = sl.getKd(); // подала МО

            return Math.min(kdMo, kdMax);
        }
    }
}
