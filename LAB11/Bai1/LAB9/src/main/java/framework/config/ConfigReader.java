package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class ConfigReader {
    private static volatile ConfigReader instance;

    private final String env;
    private final Properties properties;

    private ConfigReader() {
        this.env = System.getProperty("env", "dev").trim();
        this.properties = new Properties();

        String resourceName = "config-" + env + ".properties";
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IllegalStateException("Missing config file in classpath: " + resourceName);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config: " + resourceName, e);
        }

        System.out.println("[Config] Dang dung moi truong: " + env);
        System.out.println("[Config] explicit.wait.seconds = " + getExplicitWaitSeconds());
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    public String getEnv() {
        return env;
    }

    public String getBaseUrl() {
        String baseUrl = System.getenv("BASE_URL");
        if (baseUrl != null && !baseUrl.isBlank()) {
            return baseUrl.trim();
        }
        return require("base.url");
    }

    public int getExplicitWaitSeconds() {
        return Integer.parseInt(require("explicit.wait.seconds"));
    }

    public int getRetryCount() {
        return Integer.parseInt(properties.getProperty("retry.count", "0").trim());
    }

    public String getUsername() {
        return requireCredential("username", "APP_USERNAME");
    }

    public String getPassword() {
        return requireCredential("password", "APP_PASSWORD");
    }

    private String requireCredential(String propertyKey, String envVar) {
        String fromEnv = System.getenv(envVar);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.trim();
        }

        String fromProps = properties.getProperty(propertyKey);
        if (fromProps != null && !fromProps.trim().isEmpty()) {
            return fromProps.trim();
        }

        throw new IllegalStateException(
                "Missing credential. Set environment variable '" + envVar + "' " +
                        "or provide non-empty key '" + propertyKey + "' in config-" + env + ".properties");
    }

    private String require(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required config key: " + key + " (env=" + env + ")");
        }
        return Objects.requireNonNull(value).trim();
    }
}
