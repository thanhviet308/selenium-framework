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
        String b = (browser == null || browser.isBlank()) ? "chrome" : browser.trim().toLowerCase();

        boolean isCI = System.getenv("CI") != null;
        String osName = System.getProperty("os.name", "").toLowerCase();
        boolean isLinux = osName.contains("linux");
        boolean hasDisplay = System.getenv("DISPLAY") != null || System.getenv("WAYLAND_DISPLAY") != null;
        String defaultHeadless = (isCI || (isLinux && !hasDisplay)) ? "true" : "false";
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", defaultHeadless));

        switch (b) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                    options.addArguments("--width=1920");
                    options.addArguments("--height=1080");
                }
                return new FirefoxDriver(options);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                }
                if (isLinux && (isCI || headless)) {
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                }
                return new EdgeDriver(options);
            }
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                }
                if (isLinux && (isCI || headless)) {
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                }
                return new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}
