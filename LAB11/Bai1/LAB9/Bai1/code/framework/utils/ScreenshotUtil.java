package framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {
    private ScreenshotUtil() {
    }

    public static String capture(WebDriver driver, String testName) {
        try {
            Path dir = Path.of("target", "screenshots");
            Files.createDirectories(dir);

            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String safeName = (testName == null ? "test" : testName).replaceAll("[^a-zA-Z0-9._-]", "_");
            Path file = dir.resolve(safeName + "_" + ts + ".png");

            File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(tmp.toPath(), file);
            System.out.println("[Screenshot] " + file.toAbsolutePath());
            return file.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to capture screenshot", e);
        }
    }

    public static byte[] captureAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
