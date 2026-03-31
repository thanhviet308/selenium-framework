package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {
    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillInformation(String firstName, String lastName, String postalCode) {
        waitAndType(firstNameField, firstName);
        waitAndType(lastNameField, lastName);
        waitAndType(postalCodeField, postalCode);
        return this;
    }

    public CheckoutOverviewPage continueToOverview() {
        waitAndClick(continueButton);
        waitForPageLoad();
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
        return new CheckoutOverviewPage(driver);
    }
}
