package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class InventoryPage extends BasePage {
    @FindBy(css = ".inventory_list")
    private WebElement inventoryList;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(css = ".inventory_item button")
    private List<WebElement> addToCartButtons;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".inventory_list"));
    }

    public InventoryPage addFirstItemToCart() {
        if (addToCartButtons == null || addToCartButtons.isEmpty()) {
            return this;
        }
        waitAndClick(addToCartButtons.get(0));
        return this;
    }

    public InventoryPage addItemByName(String name) {
        if (inventoryItems == null) {
            return this;
        }

        for (WebElement item : inventoryItems) {
            try {
                String itemName = item.findElement(By.cssSelector(".inventory_item_name")).getText().trim();
                if (itemName.equalsIgnoreCase(name.trim())) {
                    WebElement button = item.findElement(By.cssSelector("button"));
                    waitAndClick(button);
                    return this;
                }
            } catch (NoSuchElementException ignored) {
            }
        }
        throw new IllegalArgumentException("Item not found by name: " + name);
    }

    public int getCartItemCount() {
        try {
            String text = cartBadge.getText().trim();
            if (text.isBlank()) {
                return 0;
            }
            return Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage goToCart() {
        waitAndClick(cartLink);
        return new CartPage(driver);
    }
}
