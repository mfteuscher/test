package edu.byu.cs.tweeter.client.presenter;

import android.text.Editable;

import edu.byu.cs.tweeter.client.presenter.view.AuthenticatedView;

public class LoginPresenter extends AuthenticationPresenter {


    public LoginPresenter(AuthenticatedView view) {
        super(view);
    }

    @Override
    protected String getMessage() {
        return "login";
    }

    public void login(String alias, String password){
        userService.login(alias, password, new AuthenticatedObserver());
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

}
