package framework.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Creates the required Excel file (login_data.xlsx) if it does not exist yet.
 * This keeps the lab runnable even if the Excel file is missing initially.
 */
public final class ExcelTestDataBootstrap {
    private ExcelTestDataBootstrap() {
    }

    public static Path excelPath() {
        return Path.of("src", "test", "resources", "testdata", "login_data.xlsx");
    }

    public static void ensureLoginDataExcelExists() {
        Path path = excelPath();
        if (Files.exists(path)) {
            return;
        }

        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create testdata directory", e);
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            createSmokeSheet(workbook);
            createNegativeSheet(workbook);
            createBoundarySheet(workbook);

            try (FileOutputStream out = new FileOutputStream(path.toFile())) {
                workbook.write(out);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Excel test data: " + path, e);
        }

        System.out.println("[Excel] Generated: " + path.toAbsolutePath());
    }

    private static void createSmokeSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("SmokeCases");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("username");
        header.createCell(1).setCellValue("password");
        header.createCell(2).setCellValue("expected_url");
        header.createCell(3).setCellValue("description");

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("standard_user");
        r1.createCell(1).setCellValue("secret_sauce");
        r1.createCell(2).setCellValue("inventory");
        r1.createCell(3).setCellValue("Smoke - Login success (standard_user)");

        Row r2 = sheet.createRow(2);
        r2.createCell(0).setCellValue("problem_user");
        r2.createCell(1).setCellValue("secret_sauce");
        r2.createCell(2).setCellValue("inventory");
        r2.createCell(3).setCellValue("Smoke - Login success (problem_user)");

        Row r3 = sheet.createRow(3);
        r3.createCell(0).setCellValue("performance_glitch_user");
        r3.createCell(1).setCellValue("secret_sauce");
        r3.createCell(2).setCellValue("inventory");
        r3.createCell(3).setCellValue("Smoke - Login success (performance_glitch_user)");
    }

    private static void createNegativeSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("NegativeCases");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("username");
        header.createCell(1).setCellValue("password");
        header.createCell(2).setCellValue("expected_error");
        header.createCell(3).setCellValue("description");

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("locked_out_user");
        r1.createCell(1).setCellValue("secret_sauce");
        r1.createCell(2).setCellValue("Epic sadface: Sorry, this user has been locked out.");
        r1.createCell(3).setCellValue("Negative - Locked user");

        Row r2 = sheet.createRow(2);
        r2.createCell(0).setCellValue("");
        r2.createCell(1).setCellValue("secret_sauce");
        r2.createCell(2).setCellValue("Epic sadface: Username is required");
        r2.createCell(3).setCellValue("Negative - Empty username");

        Row r3 = sheet.createRow(3);
        r3.createCell(0).setCellValue("standard_user");
        r3.createCell(1).setCellValue("");
        r3.createCell(2).setCellValue("Epic sadface: Password is required");
        r3.createCell(3).setCellValue("Negative - Empty password");

        Row r4 = sheet.createRow(4);
        r4.createCell(0).setCellValue("standard_user");
        r4.createCell(1).setCellValue("wrongpass");
        r4.createCell(2).setCellValue("Epic sadface: Username and password do not match any user in this service");
        r4.createCell(3).setCellValue("Negative - Wrong password");

        Row r5 = sheet.createRow(5);
        r5.createCell(0).setCellValue("unknown_user");
        r5.createCell(1).setCellValue("secret_sauce");
        r5.createCell(2).setCellValue("Epic sadface: Username and password do not match any user in this service");
        r5.createCell(3).setCellValue("Negative - Unknown user");
    }

    private static void createBoundarySheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("BoundaryCases");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("username");
        header.createCell(1).setCellValue("password");
        header.createCell(2).setCellValue("expected_error");
        header.createCell(3).setCellValue("description");

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("a".repeat(200));
        r1.createCell(1).setCellValue("secret_sauce");
        r1.createCell(2).setCellValue("Epic sadface: Username and password do not match any user in this service");
        r1.createCell(3).setCellValue("Boundary - Very long username");

        Row r2 = sheet.createRow(2);
        r2.createCell(0).setCellValue("<script>alert(1)</script>");
        r2.createCell(1).setCellValue("secret_sauce");
        r2.createCell(2).setCellValue("Epic sadface: Username and password do not match any user in this service");
        r2.createCell(3).setCellValue("Boundary - XSS-like username");

        Row r3 = sheet.createRow(3);
        r3.createCell(0).setCellValue("' OR 1=1 --");
        r3.createCell(1).setCellValue("secret_sauce");
        r3.createCell(2).setCellValue("Epic sadface: Username and password do not match any user in this service");
        r3.createCell(3).setCellValue("Boundary - SQL pattern username");

        Row r4 = sheet.createRow(4);
        r4.createCell(0).setCellValue("standard_user");
        r4.createCell(1).setCellValue("p".repeat(200));
        r4.createCell(2).setCellValue("Epic sadface: Username and password do not match any user in this service");
        r4.createCell(3).setCellValue("Boundary - Very long password");
    }
}
