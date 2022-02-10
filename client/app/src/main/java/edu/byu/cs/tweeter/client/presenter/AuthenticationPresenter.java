package edu.byu.cs.tweeter.client.presenter;

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
            return getMessage();
        }
    }

    protected abstract String getMessage();

    private AuthenticatedView getView() {
        return (AuthenticatedView) view;
    }


}
