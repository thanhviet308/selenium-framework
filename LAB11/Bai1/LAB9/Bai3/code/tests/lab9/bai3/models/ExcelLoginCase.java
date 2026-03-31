package lab9.bai3.models;

public record ExcelLoginCase(
        String sheet,
        String username,
        String password,
        String expectedUrl,
        String expectedError,
        String description
) {
    public boolean isSuccessCase() {
        return expectedUrl != null && !expectedUrl.isBlank();
    }
}
