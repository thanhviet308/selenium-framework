package framework.base;

import framework.config.ConfigReader;
import framework.driver.DriverFactory;
import framework.utils.ExcelTestDataBootstrap;
import framework.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseTest {
    private final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final AtomicBoolean BOOTSTRAPPED = new AtomicBoolean(false);

    protected WebDriver getDriver() {
        return tlDriver.get();
    }

    @BeforeSuite(alwaysRun = true)
    public void bootstrapSuite() {
        if (BOOTSTRAPPED.compareAndSet(false, true)) {
            ExcelTestDataBootstrap.ensureLoginDataExcelExists();
        }
    }

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        String resolvedEnv = (env == null || env.isBlank()) ? System.getProperty("env", "dev") : env;
        if (resolvedEnv == null || resolvedEnv.isBlank()) {
            resolvedEnv = "dev";
        }
        String resolvedBrowser = (browser == null || browser.isBlank()) ? System.getProperty("browser", "chrome") : browser;
        if (resolvedBrowser == null || resolvedBrowser.isBlank()) {
            resolvedBrowser = "chrome";
        }

        System.setProperty("env", resolvedEnv);
        ConfigReader config = ConfigReader.getInstance();

        WebDriver driver = DriverFactory.createDriver(resolvedBrowser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(config.getBaseUrl());
        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE && getDriver() != null) {
                String displayName = result.getTestName();
                if (displayName == null || displayName.isBlank()) {
                    displayName = result.getName();
                }
                ScreenshotUtil.capture(getDriver(), displayName);
            }
        } finally {
            if (getDriver() != null) {
                getDriver().quit();
                tlDriver.remove();
            }
        }
    }
}
