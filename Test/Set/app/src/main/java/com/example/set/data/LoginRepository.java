package com.example.set.data;

import com.example.set.data.model.LoggedInUser;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    private LoggedInUser user = null;

    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public Result<LoggedInUser> login(String username) {
        Result<LoggedInUser> result = dataSource.login(username);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}
