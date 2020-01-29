package com.example.set.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private String token;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success, @Nullable String token) {
        this.success = success;
        this.token = token;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    String getToken() {
        return token;
    }

    @Nullable
    String getError() {
        return error;
    }
}
