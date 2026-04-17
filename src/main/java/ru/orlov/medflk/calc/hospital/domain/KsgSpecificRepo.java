package ru.orlov.medflk.calc.hospital.domain;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Getter
@Log4j2
@Service
public class KsgSpecificRepo {

    private final Map<String, BigDecimal> ksgKsMap = new HashMap<>(); // ТС Приложение 18
    private final Set<String> ksgSurgeryList = new HashSet<>(); // МР Приложение 4
    private final Set<String> ksgKusIgnoredList = new HashSet<>(); // МР Приложение 5
    private final Set<String> ksgShortList = new HashSet<>(); // ПГГ Приложение 8

    public KsgSpecificRepo() {
        readKsgKs();
        readSurgeryList();
        readKusIgnoredList();
        readShortList();
    }

    public Boolean isSurgeryOrTromb(String nKsg) {
        return ksgSurgeryList.contains(nKsg);
    }

    public BigDecimal getKsgKs(String nKsg) {
        return ksgKsMap.getOrDefault(nKsg, BigDecimal.ONE);
    }

    private void readKsgKs() {
        try (
                Stream<String> lines = Files.lines(Paths.get("nsi/ТС_Приложение_18.csv"))
        ) {
            lines.forEach(line -> {
                if (line.startsWith("#")) return;
                String[] cells = line.split(";");
                String nKsg = cells[0];
                BigDecimal ksKsg = new BigDecimal(cells[1]);
                ksgKsMap.put(nKsg, ksKsg);
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void readSurgeryList() {
        try (
                Stream<String> lines = Files.lines(Paths.get("nsi/МР_Приложение_4.txt"))
        ) {
            lines.forEach(line -> {
                if (line.startsWith("#")) return;
                String[] cells = line.split("\\s+");
                ksgSurgeryList.addAll(Arrays.stream(cells).toList());
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void readKusIgnoredList() {
        try (
                Stream<String> lines = Files.lines(Paths.get("nsi/МР_Приложение_5.txt"))
        ) {
            lines.forEach(line -> {
                if (line.startsWith("#")) return;
                String[] cells = line.split("\\s+");
                ksgKusIgnoredList.addAll(Arrays.stream(cells).toList());
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void readShortList() {
        try (
                Stream<String> lines = Files.lines(Paths.get("nsi/ПГГ_Приложение_8.txt"))
        ) {
            lines.forEach(line -> {
                if (line.startsWith("#")) return;
                String[] cells = line.split("\\s+");
                ksgShortList.addAll(Arrays.stream(cells).toList());
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
