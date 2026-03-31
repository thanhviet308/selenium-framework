package lab9.bai6;

import framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlakySimulationTest extends BaseTest {
    private static int callCount = 0;

    @Test(groups = {"regression"}, description = "Flaky simulation: fail 2 times, pass on 3rd")
    public void testFlakyScenario() {
        callCount++;
        System.out.println("[FlakyTest] Dang chay lan thu: " + callCount);
        if (callCount <= 2) {
            Assert.fail("Mo phong loi mang tam thoi - lan " + callCount);
        }
        Assert.assertTrue(true, "Test pass o lan thu " + callCount);
    }
}
