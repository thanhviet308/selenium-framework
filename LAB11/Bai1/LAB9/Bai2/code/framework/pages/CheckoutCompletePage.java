package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends BasePage {
    @FindBy(css = ".complete-header")
    private WebElement completeHeader;

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(completeHeader));
            return true;
        } catch (TimeoutException e) {
            return isElementVisible(By.cssSelector(".complete-header"));
        }
    }

    public String getCompleteHeader() {
        return getText(completeHeader);
    }
}
