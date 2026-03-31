package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        String resolvedBrowser = (browser == null || browser.isBlank()) ? "chrome" : browser.trim().toLowerCase();
        boolean isCI = System.getenv("CI") != null;
        boolean headless = isCI || Boolean.parseBoolean(System.getProperty("headless", "false"));

        switch (resolvedBrowser) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                    if (isCI) {
                        options.addArguments("--no-sandbox");
                        options.addArguments("--disable-dev-shm-usage");
                    }
                } else {
                    options.addArguments("--start-maximized");
                }
                return new ChromeDriver(options);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                }
                return new EdgeDriver(options);
            }
            case "firefox" -> {
                return createFirefoxDriver(headless);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless)
            options.addArguments("-headless");
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }
}