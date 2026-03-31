package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {
    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        try {
            return cartItems == null ? 0 : cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage removeFirstItem() {
        if (removeButtons == null || removeButtons.isEmpty()) {
            return this;
        }
        waitAndClick(removeButtons.get(0));
        return this;
    }

    public CheckoutPage goToCheckout() {
        waitAndClick(checkoutButton);
        return new CheckoutPage(driver);
    }

    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        if (cartItems == null) {
            return names;
        }
        for (WebElement item : cartItems) {
            try {
                names.add(item.findElement(By.cssSelector(".inventory_item_name")).getText().trim());
            } catch (NoSuchElementException ignored) {
            }
        }
        return names;
    }
}
