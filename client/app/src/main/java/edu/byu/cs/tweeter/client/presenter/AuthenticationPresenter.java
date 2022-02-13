package edu.byu.cs.tweeter.client.presenter;

import android.text.Editable;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticatedView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticationPresenter extends Presenter {

    protected AuthenticationPresenter(AuthenticatedView view) {
        super(view);
    }

    protected class AuthenticatedObserver extends Observer implements UserObserver {

        @Override
        public void handleSuccess(User user) {
            getView().authenticated(user);
        }

        @Override
        protected String getActionType() {
            return getAuthenticationActionType();
        }
    }

    protected abstract String getAuthenticationActionType();

    private AuthenticatedView getView() {
        return (AuthenticatedView) view;
    }

    public void validate(Editable alias, Editable password) throws IllegalArgumentException {
        if (isFieldEmpty(alias)) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (isFieldEmpty(password)) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    protected boolean isFieldEmpty(Editable editable) { return editable.length() == 0; }

}
