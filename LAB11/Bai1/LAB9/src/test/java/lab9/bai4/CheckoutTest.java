package lab9.bai4;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
import framework.pages.CheckoutCompletePage;
import framework.pages.CheckoutOverviewPage;
import framework.pages.CheckoutPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.TestDataFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class CheckoutTest extends BaseTest {

    @Test(groups = {"regression"}, description = "Checkout uses random Faker data")
    public void testCheckoutWithRandomData() {
        Map<String, String> data = TestDataFactory.randomCheckoutData();
        System.out.println("[Faker] Checkout data: " + data);

        InventoryPage inventoryPage = new LoginPage(getDriver()).login(
                ConfigReader.getInstance().getUsername(),
                ConfigReader.getInstance().getPassword()
        );

        CartPage cartPage = inventoryPage.addFirstItemToCart().goToCart();
        CheckoutPage checkoutPage = cartPage.goToCheckout();
        CheckoutOverviewPage overview = checkoutPage
                .fillInformation(data.get("firstName"), data.get("lastName"), data.get("postalCode"))
                .continueToOverview();

        CheckoutCompletePage complete = overview.finish();
        Assert.assertTrue(complete.isLoaded(), "Complete page did not load");
        Assert.assertTrue(complete.getCompleteHeader().toLowerCase().contains("thank"), "Expected a thank-you message");
    }
}
