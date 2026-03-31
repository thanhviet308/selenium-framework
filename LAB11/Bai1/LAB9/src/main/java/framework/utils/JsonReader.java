package framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import framework.models.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class JsonReader {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String USERNAME_PLACEHOLDER = "${APP_USERNAME}";
    private static final String PASSWORD_PLACEHOLDER = "${APP_PASSWORD}";

    private JsonReader() {
    }

    public static List<UserData> readUsers(String resourcePath) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Missing JSON resource: " + resourcePath);
            }
            List<UserData> users = mapper.readValue(in, new TypeReference<List<UserData>>() {
            });
            return resolveCredentialPlaceholders(users);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON: " + resourcePath, e);
        }
    }

    private static List<UserData> resolveCredentialPlaceholders(List<UserData> users) {
        String envUser = System.getenv("APP_USERNAME");
        String envPass = System.getenv("APP_PASSWORD");

        for (UserData u : users) {
            if (u == null) {
                continue;
            }
            if (USERNAME_PLACEHOLDER.equals(u.getUsername())) {
                if (envUser == null || envUser.isBlank()) {
                    throw new IllegalStateException("Missing env APP_USERNAME required for JSON test data");
                }
                u.setUsername(envUser.trim());
            }
            if (PASSWORD_PLACEHOLDER.equals(u.getPassword())) {
                if (envPass == null || envPass.isBlank()) {
                    throw new IllegalStateException("Missing env APP_PASSWORD required for JSON test data");
                }
                u.setPassword(envPass.trim());
            }
        }
        return users;
    }
}
