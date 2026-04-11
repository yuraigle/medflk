package ru.orlov.medflk.calc.hospital;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Repository
public class KsgGrouperRepo {

    private final List<GroupKsg> groupKsgList = new ArrayList<>();
    private final Map<String, GroupKsgData> ksgData = new HashMap<>();

    public KsgGrouperRepo() {
        File nsiDir = new File("nsi");
        if (!nsiDir.exists() && !nsiDir.mkdir()) {
            throw new RuntimeException("Не удалось создать каталог " + nsiDir.getAbsolutePath());
        }

        readExcelGrouper("nsi/КСГ26_КС.xlsx");
        readExcelGrouper("nsi/КСГ26_ДС.xlsx");
    }

    public List<GroupKsg> getGrouper(Integer uslOk) {
        return groupKsgList.stream()
                .filter(g -> g.getNKsg().startsWith(uslOk == 1 ? "st" : "ds"))
                .toList();
    }

    public GroupKsgData getKsgData(String nKsg) {
        return ksgData.get(nKsg);
    }

    private void readExcelGrouper(String filename) {
        try (
                FileInputStream fis = new FileInputStream(filename);
                Workbook workbook = new XSSFWorkbook(fis)
        ) {
            readGrouperSheet(workbook.getSheet("Группировщик"));
            readKsgSheet(workbook.getSheet("КСГ"));
        } catch (IOException e) {
            log.error("Ошибка чтения файла {}, расчёт стоимости отключен", filename);
        }
    }

    private void readGrouperSheet(Sheet sheet) {
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            GroupKsg group = new GroupKsg();
            group.setN(getIntCell(row, 0));        // A
            group.setDs1(getStringCell(row, 1));
            group.setDs2(getStringCell(row, 2));
            group.setDs3(getStringCell(row, 3));
            group.setCodeUsl(getStringCell(row, 4));
            group.setAge(getIntCell(row, 5));
            group.setSex(getIntCell(row, 6));
            group.setKd(getIntCell(row, 7));
            group.setDkk(getStringCell(row, 8));
            group.setFrak(getStringCell(row, 9));
            group.setNKsg(getStringCell(row, 10));
            group.setNgr(getIntCell(row, 11));     // L

            groupKsgList.add(group);
        }
    }

    private void readKsgSheet(Sheet sheet) {
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            GroupKsgData d = new GroupKsgData();
            d.setNKsg(getStringCell(row, 1));   // B
            d.setKoefZ(getDecimalCell(row, 3)); // D
            d.setProfil(getIntCell(row, 4));    // E
            d.setDolZp(getDecimalCell(row, 6)); // G

            ksgData.put(d.getNKsg(), d);
        }
    }

    private String getStringCell(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double val = cell.getNumericCellValue();
                yield val == Math.floor(val) ? String.valueOf((long) val) : String.valueOf(val);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCachedFormulaResultType() == CellType.NUMERIC
                    ? String.valueOf((long) cell.getNumericCellValue())
                    : cell.getStringCellValue().trim();
            default -> null;
        };
    }

    private Integer getIntCell(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case NUMERIC, FORMULA -> (int) cell.getNumericCellValue();
            case STRING -> {
                String val = cell.getStringCellValue().trim();
                yield val.isEmpty() ? null : Integer.parseInt(val);
            }
            default -> null;
        };
    }

    private BigDecimal getDecimalCell(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case NUMERIC, FORMULA -> BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING -> {
                String val = cell.getStringCellValue().trim();
                yield val.isEmpty() ? null : new BigDecimal(val);
            }
            default -> null;
        };
    }
}
