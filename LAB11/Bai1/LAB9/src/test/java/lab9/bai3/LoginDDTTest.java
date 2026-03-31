package lab9.bai3;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import framework.utils.ExcelTestDataBootstrap;
import lab9.bai3.models.ExcelLoginCase;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

public class LoginDDTTest extends BaseTest implements ITest {
    private static final ThreadLocal<String> TEST_NAME = new ThreadLocal<>();

    @Override
    public String getTestName() {
        return TEST_NAME.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setDynamicTestName(Method method, Object[] testData) {
        if (testData != null && testData.length > 0 && testData[0] instanceof ExcelLoginCase c) {
            TEST_NAME.set(c.description());
        } else {
            TEST_NAME.set(method.getName());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void clearTestName() {
        TEST_NAME.remove();
    }

    @DataProvider(name = "loginFromExcel", parallel = true)
    public Object[][] loginFromExcel(ITestContext context) {
        Set<String> includedGroups = new HashSet<>(Arrays.asList(context.getIncludedGroups()));
        boolean isSmoke = includedGroups.contains("smoke");

        Path excel = ExcelTestDataBootstrap.excelPath();

        List<ExcelLoginCase> cases = new ArrayList<>();
        if (isSmoke) {
            cases.addAll(readSmokeCases(excel));
        } else {
            cases.addAll(readSmokeCases(excel));
            cases.addAll(readErrorCases(excel, "NegativeCases"));
            cases.addAll(readErrorCases(excel, "BoundaryCases"));
        }

        Object[][] data = new Object[cases.size()][1];
        for (int i = 0; i < cases.size(); i++) {
            data[i][0] = cases.get(i);
        }
        return data;
    }

    @Test(groups = {"smoke", "regression"}, dataProvider = "loginFromExcel")
    public void testLoginFromExcel(ExcelLoginCase c) {
        LoginPage loginPage = new LoginPage(getDriver());

        if (c.isSuccessCase()) {
            InventoryPage inventoryPage = loginPage.login(c.username(), c.password());
            Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page did not load");
            Assert.assertTrue(getDriver().getCurrentUrl().contains(c.expectedUrl()), "Expected URL fragment: " + c.expectedUrl());
        } else {
            loginPage.loginExpectingFailure(c.username(), c.password());
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be displayed");
            Assert.assertEquals(loginPage.getErrorMessage(), c.expectedError(), "Error message mismatch");
        }
    }

    private static List<ExcelLoginCase> readSmokeCases(Path excel) {
        List<Map<String, String>> rows = ExcelReader.readSheet(excel, "SmokeCases");
        List<ExcelLoginCase> cases = new ArrayList<>();
        for (Map<String, String> row : rows) {
            cases.add(new ExcelLoginCase(
                    "SmokeCases",
                    row.getOrDefault("username", ""),
                    row.getOrDefault("password", ""),
                    row.getOrDefault("expected_url", ""),
                    "",
                    row.getOrDefault("description", "SmokeCases")
            ));
        }
        return cases;
    }

    private static List<ExcelLoginCase> readErrorCases(Path excel, String sheetName) {
        List<Map<String, String>> rows = ExcelReader.readSheet(excel, sheetName);
        List<ExcelLoginCase> cases = new ArrayList<>();
        for (Map<String, String> row : rows) {
            cases.add(new ExcelLoginCase(
                    sheetName,
                    row.getOrDefault("username", ""),
                    row.getOrDefault("password", ""),
                    "",
                    row.getOrDefault("expected_error", ""),
                    row.getOrDefault("description", sheetName)
            ));
        }
        return cases;
    }
}
