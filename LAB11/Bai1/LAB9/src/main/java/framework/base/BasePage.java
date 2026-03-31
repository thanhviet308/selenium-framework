package framework.base;

import framework.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;

        int timeoutSeconds = ConfigReader.getInstance().getExplicitWaitSeconds();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        this.wait.ignoring(StaleElementReferenceException.class);

        PageFactory.initElements(driver, this);
    }

    /**
     * Click an element only after it becomes clickable (prevents ElementNotInteractable and timing flakiness).
     */
    protected void waitAndClick(WebElement element) {
        WebElement clickable = wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            clickable.click();
        } catch (WebDriverException e) {
            scrollToElement(clickable);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickable);
        }
    }

    /**
     * Type into an input only after it is visible; clears existing text first to avoid concatenated input.
     */
    protected void waitAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get visible text from an element (waits for visibility) and trims whitespace.
     */
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    /**
     * Safe visibility check by locator; returns false instead of throwing when element is missing or becomes stale.
     */
    protected boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Scrolls the page to bring the element into the viewport (useful when element is outside view).
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Wait until the browser reports the document is fully loaded (readyState=complete).
     */
    protected void waitForPageLoad() {
        wait.until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    /**
     * Reads an attribute value from an element after ensuring the element is visible.
     */
    protected String getAttribute(WebElement element, String attribute) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attribute);
    }
}
