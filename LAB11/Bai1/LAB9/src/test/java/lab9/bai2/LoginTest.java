package lab9.bai2;

import framework.base.BaseTest;
import framework.config.ConfigReader;
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

public class LoginTest extends BaseTest {

    @Test(groups = { "smoke", "regression" }, description = "Login success navigates to inventory")
    @Feature("Đăng nhập hệ thống")
    @Story("UC-001: Đăng nhập bằng tài khoản hợp lệ")
    @Description("Kiểm thử đăng nhập với username/password hợp lệ")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginSuccess() {
        ConfigReader config = ConfigReader.getInstance();

        Allure.step("Mở trang đăng nhập", () -> getDriver().get(config.getBaseUrl()));

        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = Allure.step("Nhập thông tin đăng nhập và đăng nhập",
                () -> loginPage.login(config.getUsername(), config.getPassword()));

        Allure.step("Kiểm tra chuyển trang thành công", () -> {
            Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load");
            Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"), "URL should contain inventory");
        });
    }

    @Test(groups = { "regression" }, description = "Locked out user shows error")
    @Feature("Đăng nhập hệ thống")
    @Story("UC-002: Tài khoản bị khóa")
    @Description("Kiểm thử đăng nhập với tài khoản bị khóa hiển thị thông báo lỗi")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginLockedUserShowsError() {
        ConfigReader config = ConfigReader.getInstance();
        Allure.step("Mở trang đăng nhập", () -> getDriver().get(config.getBaseUrl()));

        LoginPage loginPage = new LoginPage(getDriver());
        Allure.step("Nhập username bị khóa + password",
                () -> loginPage.loginExpectingFailure("locked_out_user", config.getPassword()));

        Allure.step("Kiểm tra hiển thị lỗi locked out", () -> {
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
            Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"),
                    "Error should mention locked out");
        });
    }

    @Test(groups = { "regression" }, description = "Empty username shows required message")
    @Feature("Đăng nhập hệ thống")
    @Story("UC-003: Thiếu username")
    @Description("Kiểm thử đăng nhập khi bỏ trống username hiển thị thông báo bắt buộc")
    @Severity(SeverityLevel.MINOR)
    public void testLoginEmptyUsername() {
        ConfigReader config = ConfigReader.getInstance();
        Allure.step("Mở trang đăng nhập", () -> getDriver().get(config.getBaseUrl()));

        LoginPage loginPage = new LoginPage(getDriver());
        Allure.step("Để trống username và nhập password",
                () -> loginPage.loginExpectingFailure("", config.getPassword()));

        Allure.step("Kiểm tra hiển thị lỗi yêu cầu username", () -> {
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
            Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("username"),
                    "Error should mention username");
        });
    }

    @Test(groups = { "regression" }, description = "Wrong password shows mismatch message")
    @Feature("Đăng nhập hệ thống")
    @Story("UC-004: Sai password")
    @Description("Kiểm thử đăng nhập với password sai hiển thị thông báo mismatch")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWrongPassword() {
        ConfigReader config = ConfigReader.getInstance();
        Allure.step("Mở trang đăng nhập", () -> getDriver().get(config.getBaseUrl()));

        LoginPage loginPage = new LoginPage(getDriver());
        Allure.step("Nhập username hợp lệ + password sai",
                () -> loginPage.loginExpectingFailure(config.getUsername(), "wrongpass"));

        Allure.step("Kiểm tra hiển thị lỗi password mismatch", () -> {
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
            Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("do not match"),
                    "Error should mention mismatch");
        });
    }
}
