package com.example.set.data;

import com.example.set.data.model.LoggedInUser;
import com.example.set.data.model.RegisterResponse;

import java.io.IOException;

public class LoginDataSource {

    final private Api api = new Api();

    public Result<LoggedInUser> login(String username) {

        String registerData = "{\"action\": \"register\", \"nickname\": \"" + username + "\"}";

        try {
            RegisterResponse registerResponse = api.call(registerData, RegisterResponse.class);
            if (registerResponse.isSuccess()) {
                LoggedInUser user =
                        new LoggedInUser(
                                registerResponse.getToken(),
                                username);
                return new Result.Success<>(user);
            }
            return new Result.Error(new IOException("Login failed"));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Connection error", e));
        }
    }
}
