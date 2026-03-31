package lab9.bai4;

import framework.base.BaseTest;
import framework.models.UserData;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.JsonReader;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

public class UserLoginTest extends BaseTest implements ITest {
    private static final ThreadLocal<String> TEST_NAME = new ThreadLocal<>();

    @Override
    public String getTestName() {
        return TEST_NAME.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setDynamicTestName(Method method, Object[] testData) {
        if (testData != null && testData.length > 0 && testData[0] instanceof UserData u) {
            TEST_NAME.set(u.getDescription());
        } else {
            TEST_NAME.set(method.getName());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void clearTestName() {
        TEST_NAME.remove();
    }

    @DataProvider(name = "usersFromJson", parallel = true)
    public Object[][] usersFromJson() {
        List<UserData> users = JsonReader.readUsers("testdata/users.json");
        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i);
        }
        return data;
    }

    @Test(groups = {"regression"}, dataProvider = "usersFromJson")
    public void testLoginFromJson(UserData user) {
        LoginPage loginPage = new LoginPage(getDriver());
        if (user.isExpectSuccess()) {
            InventoryPage inventoryPage = loginPage.login(user.getUsername(), user.getPassword());
            Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load");
        } else {
            loginPage.loginExpectingFailure(user.getUsername(), user.getPassword());
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
        }
    }
}
