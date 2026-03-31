package lab9.bai2;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CartTest extends BaseTest {

    @Test(groups = { "smoke", "regression" }, description = "Add first item to cart increases badge count")
    @Feature("Giỏ hàng")
    @Story("UC-010: Thêm sản phẩm vào giỏ")
    @Description("Kiểm thử thêm sản phẩm đầu tiên vào giỏ hàng và kiểm tra badge")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddFirstItemToCart() {
        ConfigReader config = ConfigReader.getInstance();
        Allure.step("Mở trang đăng nhập", () -> getDriver().get(config.getBaseUrl()));

        InventoryPage inventoryPage = Allure.step("Đăng nhập",
                () -> new LoginPage(getDriver()).login(config.getUsername(), config.getPassword()));

        Allure.step("Thêm sản phẩm đầu tiên vào giỏ", inventoryPage::addFirstItemToCart);
        Allure.step("Kiểm tra badge giỏ hàng = 1",
                () -> Assert.assertEquals(inventoryPage.getCartItemCount(), 1, "Cart badge should be 1"));
    }

    @Test(groups = { "regression" }, description = "Remove item from cart results empty cart")
    @Feature("Giỏ hàng")
    @Story("UC-011: Xóa sản phẩm khỏi giỏ")
    @Description("Kiểm thử thêm rồi xóa sản phẩm khỏi giỏ và kiểm tra giỏ trống")
    @Severity(SeverityLevel.NORMAL)
    public void testRemoveFirstItemFromCart() {
        ConfigReader config = ConfigReader.getInstance();
        Allure.step("Mở trang đăng nhập", () -> getDriver().get(config.getBaseUrl()));

        InventoryPage inventoryPage = Allure.step("Đăng nhập",
                () -> new LoginPage(getDriver()).login(config.getUsername(), config.getPassword()));

        Allure.step("Thêm sản phẩm đầu tiên", inventoryPage::addFirstItemToCart);
        CartPage cartPage = Allure.step("Đi tới trang giỏ hàng", inventoryPage::goToCart);

        Allure.step("Kiểm tra giỏ có ít nhất 1 sản phẩm",
                () -> Assert.assertTrue(cartPage.getItemCount() >= 1, "Cart should have at least 1 item"));

        Allure.step("Xóa sản phẩm đầu tiên khỏi giỏ", cartPage::removeFirstItem);
        Allure.step("Kiểm tra giỏ trống",
                () -> Assert.assertEquals(cartPage.getItemCount(), 0, "Cart should be empty after removal"));
    }

    @Test(groups = { "regression" }, description = "Cart item names can be read")
    @Feature("Giỏ hàng")
    @Story("UC-012: Đọc tên sản phẩm trong giỏ")
    @Description("Kiểm thử danh sách tên sản phẩm trong giỏ không rỗng")
    @Severity(SeverityLevel.MINOR)
    public void testCartItemNames() {
        ConfigReader config = ConfigReader.getInstance();
        Allure.step("Mở trang đăng nhập", () -> getDriver().get(config.getBaseUrl()));

        InventoryPage inventoryPage = Allure.step("Đăng nhập",
                () -> new LoginPage(getDriver()).login(config.getUsername(), config.getPassword()));

        Allure.step("Thêm sản phẩm đầu tiên", inventoryPage::addFirstItemToCart);
        CartPage cartPage = Allure.step("Đi tới trang giỏ hàng", inventoryPage::goToCart);

        List<String> names = Allure.step("Lấy danh sách tên sản phẩm", cartPage::getItemNames);
        Allure.step("Kiểm tra danh sách tên không rỗng",
                () -> Assert.assertFalse(names.isEmpty(), "Item names list should not be empty"));
    }
}
