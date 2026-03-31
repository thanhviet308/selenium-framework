package framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public final class ExcelReader {
    private ExcelReader() {
    }

    public static List<Map<String, String>> readSheet(Path excelPath, String sheetName) {
        try (FileInputStream in = new FileInputStream(excelPath.toFile());
             Workbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return List.of();
            }

            List<String> headers = new ArrayList<>();
            int lastCell = headerRow.getLastCellNum();
            for (int c = 0; c < lastCell; c++) {
                headers.add(getCellValue(headerRow.getCell(c), evaluator));
            }

            List<Map<String, String>> rows = new ArrayList<>();
            int lastRow = sheet.getLastRowNum();
            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                Map<String, String> map = new LinkedHashMap<>();
                boolean hasAnyValue = false;
                for (int c = 0; c < headers.size(); c++) {
                    String header = headers.get(c);
                    String value = getCellValue(row.getCell(c), evaluator);
                    if (!value.isBlank()) {
                        hasAnyValue = true;
                    }
                    map.put(header, value);
                }
                if (hasAnyValue) {
                    rows.add(map);
                }
            }
            return rows;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel: " + excelPath, e);
        }
    }

    /**
     * Convert a cell into String, safely handling null and 4 types: STRING, NUMERIC, BOOLEAN, FORMULA.
     */
    public static String getCellValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return "";
        }

        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) {
            type = evaluator.evaluateFormulaCell(cell);
        }

        return switch (type) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield String.valueOf(cell.getDateCellValue());
                }
                double v = cell.getNumericCellValue();
                long asLong = (long) v;
                yield (v == asLong) ? String.valueOf(asLong) : String.valueOf(v);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case BLANK, _NONE, ERROR -> "";
            default -> "";
        };
    }
}
