package edu.byu.cs.tweeter.client.presenter;

import android.text.Editable;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    private final View view;
    private final UserService userService;

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public interface View {
        void loggedIn(User user);
        void displayErrorMessage(String message);
    }

    public void login(String alias, String password){
        userService.login(alias, password, new LoginObserver());
    }

    public void validateLogin(Editable alias, Editable password) throws IllegalArgumentException {
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public class LoginObserver implements UserService.LoginObserver {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.loggedIn(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception e) {
            view.displayErrorMessage("Failed to login because of exception: " + e.getMessage());
        }
    }

}
