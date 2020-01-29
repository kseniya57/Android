package com.example.set.data.model;

public class LoggedInUser {

    private String token;
    private String displayName;

    public LoggedInUser(String token, String displayName) {
        this.token = token;
        this.displayName = displayName;
    }

    public String getToken() {
        return token;
    }

    public String getDisplayName() {
        return displayName;
    }
}
