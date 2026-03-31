package lab9.bai1;

import framework.base.BaseTest;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Bai1SanityTest extends BaseTest {

    @Test(groups = {"bai1"}, description = "Bai 1 sanity: BaseTest opens base URL and LoginPage is ready")
    public void testBaseTestAndLoginPageLoaded() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoaded(), "Login page should be loaded");
    }
}
