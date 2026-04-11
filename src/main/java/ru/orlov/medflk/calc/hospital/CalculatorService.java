package ru.orlov.medflk.calc.hospital;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.domain.nsi.V023Packet;
import ru.orlov.medflk.domain.nsi.V023Service;
import ru.orlov.medflk.jaxb.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final V023Service v023Service;
    private final KsgGrouperService ksgGrouperService;
    private final KsgMathsService ksgMathsService;

    public void calcFile(ZlList zlList, PersList persList) {
        for (Zap zap : zlList.getZapList()) {
            Integer uslOk = zap.getZSl().getUslOk();
            boolean isHosp = uslOk != null && List.of(1, 2).contains(uslOk);

            if (isHosp && zap.getNZap() == 219) {
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

            List<GroupKsg> ksgList = ksgGrouperService.findAllPossibleKsg(sl, pers, uslOk, kd);

            for (GroupKsg groupKsg : ksgList) {
                V023Packet.V023 v023 = v023Service.getKsgOnDate(groupKsg.getNKsg(), sl.getDate2());

                BigDecimal koefUp = ksgKpg.getKoefUp(); // КС_КСГ - коэффициент специфики // todo из базы
                BigDecimal dolZp = ksgGrouperService.getDolZp(v023.getNKsg());
                List<KsgKpg.SlKoef> kslp = ksgKpg.getSlKoefList(); // Коэффициенты Сложности ЛП

                BigDecimal sumWithKsg = ksgMathsService
                        .calcSumSlWithKsg(v023, basicRate, koefD, koefU, koefUp, dolZp, kslp);

                CalcData c = new CalcData();
                c.setNZap(zap.getNZap());
                c.setSlId(sl.getSlId());
                c.setNKsg(groupKsg.getNKsg());
                c.setKoefZ(v023.getKoefZ());
                c.setSumKsg(sumWithKsg);

                calcData.add(c);
            }
        }

        calcData.forEach(log::info);
    }


}
