package com.example.set.ui.login;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.set.data.LoginRepository;
import com.example.set.data.Result;
import com.example.set.data.model.LoggedInUser;
import com.example.set.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username) {
        (new AsyncLoginTask()).execute(username);

    }

    class AsyncLoginTask extends AsyncTask<String, Integer, Result> {
        @Override
        protected Result doInBackground(String... strings) {
            return loginRepository.login(strings[0]);
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (result instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName()), data.getToken()));
            } else {
                loginResult.setValue(new LoginResult(result.toString()));
            }
        }
    }

    public void loginDataChanged(String username) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return !username.trim().isEmpty();
    }
}
