package ru.orlov.medflk.calc.hospital;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.medflk.calc.hospital.domain.KsgSpecificRepo;
import ru.orlov.medflk.domain.nsi.V023Packet;
import ru.orlov.medflk.jaxb.KsgKpg;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class KsgMathsService {

    private final KsgSpecificRepo ksgSpecificRepo;

    /**
     * Приложение 3, Таблица 1
     * КСГ, к которым не применяются понижающие коэффициенты специфики.
     */
    private static final Set<String> ksKsgNoLower1 = new HashSet<>(List.of("""
            st13.002 st13.005 st13.007 st15.015 st15.016 st17.001 st17.002 st17.003 st21.005
            """.split("[ \n]+")));

    /**
     * Приложение 3, Таблица 2
     * КСГ, к которым не применяются повышающие коэффициенты специфики.
     */
    private static final Set<String> ksKsgNoGreater1 = new HashSet<>(List.of("""
            st04.001 st12.001 st16.003 st27.001 st27.003 st27.005 st27.006 st27.010 st30.004
            st31.002 st31.012 st31.018 st36.049
            """.split("[ \n]+")));


    public BigDecimal calcSumSlWithKsg(
            V023Packet.V023 ksg,
            BigDecimal basicRate, // БС - Базовая Ставка
            BigDecimal koefD, // КД - Коэффициент Дифференциации
            BigDecimal koefU, // КУС_МО - Коэффициент уровня/подуровня МО
            BigDecimal koefUp, // КС_КСГ - Коэффициент Специфики
            BigDecimal dolZp, // ДЗП - Доля ЗП в КСГ
            List<KsgKpg.SlKoef> slKoefList // КСЛП - коэффициенты сложности
    ) {
        // 3.2 Коэффициент относительной затратоёмкости КСГ (КЗ_КСГ)
        BigDecimal kzKsg = ksg.getKoefZ();

        // 3.3 Коэффициент специфики КСГ (КС_КСГ), учётом списка исключений
        BigDecimal ksKsg = koefUp == null ? BigDecimal.ONE : koefUp;
        if (ksKsg.compareTo(BigDecimal.ONE) < 0 && ksKsgNoLower1.contains(ksg.getNKsg())) {
            ksKsg = BigDecimal.ONE;
        }
        if (ksKsg.compareTo(BigDecimal.ONE) > 0 && ksKsgNoGreater1.contains(ksg.getNKsg())) {
            ksKsg = BigDecimal.ONE;
        }
        if (List.of("st19", "ds19", "st08", "ds08").contains(ksg.getNKsg().substring(0, 4))) {
            ksKsg = BigDecimal.ONE;
        }

        // КУС_МО с учётом применимости для конкретного КСГ
        BigDecimal kusMo = koefU == null ? BigDecimal.ONE : koefU;
        if (ksgSpecificRepo.getKsgKusIgnoredList().contains(ksg.getNKsg())) {
            kusMo = BigDecimal.ONE;
        }

        // Доля заработной платы (ДЗП)
        BigDecimal dZp = dolZp == null ? BigDecimal.ONE : dolZp;

        // КСЛП x КД* с учётом применимости КД
        BigDecimal kslpKd = getAppliedKslp(slKoefList, koefD);

        // БС*КЗ_КСГ*((1-ДЗП)+ДЗП*КС_КСГ*КУС_МО*КД)+БС*КД2*КСЛП (стр.12)
        return basicRate.multiply(kzKsg)
                .multiply(BigDecimal.ONE.subtract(dZp)
                        .add(dZp.multiply(ksKsg).multiply(kusMo).multiply(koefD)))
                .add(basicRate.multiply(kslpKd));
    }

    /**
     * Применяемый коэффициент сложности лечения пациента.
     * Методические рекомендации №3.5 (стр. 22)
     * Список КСЛП в Приложении №6 к ДС №1
     *
     * @return BigDecimal суммарный КСЛП с учётом КД*
     */
    private BigDecimal getAppliedKslp(List<KsgKpg.SlKoef> slKoefList, BigDecimal koefD) {
        BigDecimal kslp = BigDecimal.ZERO;

        if (slKoefList != null) {
            for (KsgKpg.SlKoef koef : slKoefList) {
                BigDecimal zsl = koef.getZSl();

                // "Проведение лекарственной терапии при ЗНО ..." без КД
                List<String> excl = List.of("sl015", "sl016", "sl017", "sl018", "sl019", "sl020");
                if (!excl.contains(koef.getIdsl())) {
                    zsl = zsl.multiply(koefD);
                }

                // Возможно применение нескольких КСЛП - суммируем их
                kslp = kslp.add(zsl);
            }
        }

        return kslp;
    }
}
