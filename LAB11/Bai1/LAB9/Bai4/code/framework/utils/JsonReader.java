package framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import framework.models.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class JsonReader {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonReader() {
    }

    public static List<UserData> readUsers(String resourcePath) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Missing JSON resource: " + resourcePath);
            }
            return mapper.readValue(in, new TypeReference<List<UserData>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON: " + resourcePath, e);
        }
    }
}
