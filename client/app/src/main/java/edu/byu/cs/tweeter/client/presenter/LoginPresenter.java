package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.AuthenticatedView;

public class LoginPresenter extends AuthenticationPresenter {


    public LoginPresenter(AuthenticatedView view) {
        super(view);
    }

    @Override
    protected String getAuthenticationActionType() {
        return "login";
    }

    public void login(String alias, String password){
        userService.login(alias, password, new AuthenticatedObserver());
    }

}
