package lab9.bai2;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Login success navigates to inventory")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login(
                ConfigReader.getInstance().getUsername(),
                ConfigReader.getInstance().getPassword()
        );

        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"), "URL should contain inventory");
    }

    @Test(groups = {"regression"}, description = "Locked out user shows error")
    public void testLoginLockedUserShowsError() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"), "Error should mention locked out");
    }

    @Test(groups = {"regression"}, description = "Empty username shows required message")
    public void testLoginEmptyUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure("", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("username"), "Error should mention username");
    }

    @Test(groups = {"regression"}, description = "Wrong password shows mismatch message")
    public void testLoginWrongPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure("standard_user", "wrongpass");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("do not match"), "Error should mention mismatch");
    }
}
