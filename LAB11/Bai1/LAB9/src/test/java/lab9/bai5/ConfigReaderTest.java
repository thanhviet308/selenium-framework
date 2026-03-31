package lab9.bai5;

import framework.config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

public class ConfigReaderTest {

    @Parameters({"env"})
    @BeforeClass(alwaysRun = true)
    public void setUp(@Optional("dev") String env) {
        System.setProperty("env", env);
        resetConfigReaderSingleton();
    }

    @Test(groups = {"bai5"}, description = "Bai 5: ConfigReader reads explicit.wait.seconds per environment")
    public void testConfigLoadsForEnv() {
        String env = System.getProperty("env", "dev");
        ConfigReader config = ConfigReader.getInstance();

        Assert.assertEquals(config.getEnv(), env, "Config env should match system property");
        if ("staging".equalsIgnoreCase(env)) {
            Assert.assertEquals(config.getExplicitWaitSeconds(), 20, "Staging explicit wait should be 20");
        } else {
            Assert.assertEquals(config.getExplicitWaitSeconds(), 15, "Dev explicit wait should be 15");
        }
    }

    private static void resetConfigReaderSingleton() {
        try {
            Field instanceField = ConfigReader.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot reset ConfigReader singleton for test", e);
        }
    }
}
