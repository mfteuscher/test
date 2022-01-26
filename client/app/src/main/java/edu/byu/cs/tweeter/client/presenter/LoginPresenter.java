package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    private View view;
    private UserService userService;

    public void login(String alias, String password) {
        userService.login(alias, password, new LoginObserver());
    }

    public interface View {

        void loggedIn(User user);

        void displayErrorMessage(String message);
    }

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
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
