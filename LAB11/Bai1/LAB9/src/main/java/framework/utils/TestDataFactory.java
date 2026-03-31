package framework.utils;

import com.github.javafaker.Faker;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;

public final class TestDataFactory {
    private static final Faker faker = new Faker(Locale.forLanguageTag("vi"));

    private TestDataFactory() {
    }

    public static String randomFirstName() {
        return asciiLettersOnly(faker.name().firstName());
    }

    public static String randomLastName() {
        return asciiLettersOnly(faker.name().lastName());
    }

    public static String randomPostalCode() {
        return faker.number().digits(5);
    }

    public static Map<String, String> randomCheckoutData() {
        return Map.of(
                "firstName", randomFirstName(),
                "lastName", randomLastName(),
                "postalCode", randomPostalCode()
        );
    }

    private static String asciiLettersOnly(String input) {
        if (input == null || input.isBlank()) {
            return "Test";
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z]", "");
        if (normalized.isBlank()) {
            return "Test";
        }
        return normalized;
    }
}
