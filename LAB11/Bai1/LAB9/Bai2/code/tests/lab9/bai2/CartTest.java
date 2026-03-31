package lab9.bai2;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CartTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Add first item to cart increases badge count")
    public void testAddFirstItemToCart() {
        InventoryPage inventoryPage = new LoginPage(getDriver()).login(
                ConfigReader.getInstance().getUsername(),
                ConfigReader.getInstance().getPassword()
        );

        inventoryPage.addFirstItemToCart();
        Assert.assertEquals(inventoryPage.getCartItemCount(), 1, "Cart badge should be 1");
    }

    @Test(groups = {"regression"}, description = "Remove item from cart results empty cart")
    public void testRemoveFirstItemFromCart() {
        InventoryPage inventoryPage = new LoginPage(getDriver()).login(
                ConfigReader.getInstance().getUsername(),
                ConfigReader.getInstance().getPassword()
        );

        inventoryPage.addFirstItemToCart();
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.getItemCount() >= 1, "Cart should have at least 1 item");

        cartPage.removeFirstItem();
        Assert.assertEquals(cartPage.getItemCount(), 0, "Cart should be empty after removal");
    }

    @Test(groups = {"regression"}, description = "Cart item names can be read")
    public void testCartItemNames() {
        InventoryPage inventoryPage = new LoginPage(getDriver()).login(
                ConfigReader.getInstance().getUsername(),
                ConfigReader.getInstance().getPassword()
        );

        inventoryPage.addFirstItemToCart();
        CartPage cartPage = inventoryPage.goToCart();
        List<String> names = cartPage.getItemNames();
        Assert.assertFalse(names.isEmpty(), "Item names list should not be empty");
    }
}
