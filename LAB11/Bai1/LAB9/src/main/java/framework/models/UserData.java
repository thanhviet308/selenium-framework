package framework.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private String role;

    @JsonProperty("expectSuccess")
    private boolean expectSuccess;

    @JsonProperty("description")
    private String description;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public boolean isExpectSuccess() {
        return expectSuccess;
    }

    public String getDescription() {
        return description;
    }
}
