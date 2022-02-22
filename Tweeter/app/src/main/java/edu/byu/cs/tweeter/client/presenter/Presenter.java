package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.view.View;

public abstract class Presenter {
    protected final View view;
    protected final UserService userService;

    protected Presenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    protected abstract class Observer implements ServiceObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to " + getActionType() + ": " + message);
        }

        @Override
        public void handleException(Exception e) {
            view.displayMessage("Failed to " + getActionType() + " because of exception: " + e.getMessage());
        }

        protected abstract String getActionType();

    }
}
