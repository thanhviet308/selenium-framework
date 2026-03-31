package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutOverviewPage extends BasePage {
    @FindBy(id = "finish")
    private WebElement finishButton;

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutCompletePage finish() {
        waitAndClick(finishButton);
        waitForPageLoad();
        wait.until(ExpectedConditions.urlContains("checkout-complete"));
        return new CheckoutCompletePage(driver);
    }
}
