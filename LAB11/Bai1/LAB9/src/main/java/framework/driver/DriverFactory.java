package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        String gridUrl = System.getProperty("grid.url");
        if (gridUrl != null && !gridUrl.isBlank()) {
            return createRemoteDriver(browser, gridUrl.trim());
        }

        String b = (browser == null || browser.isBlank()) ? "chrome" : browser.trim().toLowerCase();

        boolean isCI = System.getenv("CI") != null;
        String osName = System.getProperty("os.name", "").toLowerCase();
        boolean isLinux = osName.contains("linux");
        boolean hasDisplay = System.getenv("DISPLAY") != null || System.getenv("WAYLAND_DISPLAY") != null;
        String defaultHeadless = (isCI || (isLinux && !hasDisplay)) ? "true" : "false";
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", defaultHeadless));

        switch (b) {
            case "firefox" -> {
                return createFirefoxDriver(headless);
            }
            case "edge" -> {
                return createEdgeDriver(headless, isLinux, isCI);
            }
            case "chrome" -> {
                return createChromeDriver(headless, isLinux, isCI);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver createRemoteDriver(String browser, String gridUrl) {
        String b = (browser == null || browser.isBlank()) ? "chrome" : browser.trim().toLowerCase();

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(b);

        if ("chrome".equalsIgnoreCase(b)) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
            caps.merge(options);
        }

        try {
            URL gridEndpoint = new URL(gridUrl + "/wd/hub");
            RemoteWebDriver driver = new RemoteWebDriver(gridEndpoint, caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Grid URL khong hop le: " + gridUrl, e);
        }
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }

    private static WebDriver createChromeDriver(boolean headless, boolean isLinux, boolean isCI) {
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

    private static WebDriver createEdgeDriver(boolean headless, boolean isLinux, boolean isCI) {
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
}
